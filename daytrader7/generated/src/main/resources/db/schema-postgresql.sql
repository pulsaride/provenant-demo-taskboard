CREATE TABLE accountejb (
    creationdate TIMESTAMP(6),
    openbalance NUMERIC(14,2),
    logoutcount NUMERIC(38,0) NOT NULL,
    balance NUMERIC(14,2),
    accountid NUMERIC(38,0) NOT NULL,
    lastlogin TIMESTAMP(6),
    logincount NUMERIC(38,0) NOT NULL,
    profile_userid VARCHAR(250),
    PRIMARY KEY (accountid)
);

CREATE TABLE accountprofileejb (
    address VARCHAR(250),
    passwd VARCHAR(250),
    userid VARCHAR(250) NOT NULL,
    email VARCHAR(250),
    creditcard VARCHAR(250),
    fullname VARCHAR(250),
    PRIMARY KEY (userid)
);

CREATE TABLE keygenejb (
    keyval NUMERIC(38,0) NOT NULL,
    keyname VARCHAR(250) NOT NULL,
    PRIMARY KEY (keyname)
);

