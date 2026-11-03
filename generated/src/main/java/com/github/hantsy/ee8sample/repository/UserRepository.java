package com.github.hantsy.ee8sample.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceContext;

import com.github.hantsy.ee8sample.domain.LoginToken;
import com.github.hantsy.ee8sample.domain.User;
import com.github.hantsy.ee8sample.domain.support.AbstractRepository;
import jakarta.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserRepository extends AbstractRepository<User, Long> {

    @PersistenceContext
    EntityManager em;

    public Optional<User> findByUsername(String username) {
        Objects.requireNonNull(username);
        return this.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    protected EntityManager entityManager() {
        return em;
    }

    public Optional<User> findByLoginToken(String token, LoginToken.TokenType tokenType) {
        return this.em.createNativeQuery(User.QUERY_BY_LOGIN_TOKEN, User.class)
                .setParameter("tokenHash", token)
                .setParameter("tokenType", tokenType)
                .getResultStream()
                .findFirst();
    }
}
