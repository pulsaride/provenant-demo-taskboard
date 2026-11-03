package com.github.hantsy.ee8sample.domain;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.github.hantsy.ee8sample.domain.support.AbstractEntity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@NamedQuery(name = "LoginToken.remove", query = "DELETE"
        + "			FROM"
        + "				LoginToken _loginToken"
        + "			WHERE"
        + "				_loginToken.tokenHash = :tokenHash")
@NamedQuery(name = "LoginToken.removeExpired", query = "DELETE"
        + "			FROM"
        + "				LoginToken _loginToken"
        + "			WHERE"
        + "				_loginToken.expiredDate < CURRENT_TIMESTAMP")
public class LoginToken extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(unique = true)
    private String tokenHash;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime expiredDate;

    @Column(length = 45)
    private String ipAddress;

    @Column
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = STRING)
    private TokenType type;

    @PrePersist
    public void beforeSave() {
        createdDate = LocalDateTime.now();

        if (expiredDate == null) {
            expiredDate = LocalDateTime.now().plusMonths(1L);
        }
    }

    public enum TokenType {
        REMEMBER_ME,
        API,
        RESET_PASSWORD,
        ;
    }
}
