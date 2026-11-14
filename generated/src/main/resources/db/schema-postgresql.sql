CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT BIGINT, PRIMARY KEY (SEQ_NAME));
INSERT INTO SEQUENCE (SEQ_NAME, SEQ_COUNT) VALUES ('SEQ_GEN', 0);
CREATE TABLE logintoken (
    id BIGINT NOT NULL,
    version BIGINT,
    tokenhash VARCHAR(255) UNIQUE,
    createddate TIMESTAMP(6),
    expireddate TIMESTAMP(6),
    ipaddress VARCHAR(45),
    description VARCHAR(255),
    type VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE task (
    id BIGINT NOT NULL,
    version BIGINT,
    created_at TIMESTAMP(6),
    last_modified_at TIMESTAMP(6),
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    status VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    user_id BIGINT,
    roles VARCHAR(255)
);

CREATE TABLE users (
    id BIGINT NOT NULL,
    version BIGINT,
    username VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE logintoken ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_roles ADD FOREIGN KEY (user_id) REFERENCES users (id);
