package com.github.hantsy.ee8sample.domain;

import static com.github.hantsy.ee8sample.Constants.ROLE_USER;
import static com.github.hantsy.ee8sample.domain.User.QUERY_BY_LOGIN_TOKEN;
import static java.util.Arrays.asList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.github.hantsy.ee8sample.domain.support.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.NamedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users")
@NamedQuery(name = QUERY_BY_LOGIN_TOKEN, query = "SELECT"
        + "				_user"
        + "			FROM"
        + "				User _user"
        + "					JOIN"
        + "				_user.loginTokens _loginToken"
        + "					JOIN FETCH"
        + "				_user.loginTokens"
        + "			WHERE"
        + "				_loginToken.tokenHash = :tokenHash AND"
        + "				_loginToken.type = :tokenType AND"
        + "				_loginToken.expiredDate > CURRENT_TIMESTAMP")
public class User extends AbstractEntity {

    public static final Set<String> DEFAULT_ROLES = new HashSet<>(asList(ROLE_USER));

    public static final String QUERY_BY_LOGIN_TOKEN = "User.byLoginTokens";

    private String username;

    private String password;

    @ElementCollection
    private Set<String> roles = DEFAULT_ROLES;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    List<LoginToken> loginTokens = new ArrayList<>();
}
