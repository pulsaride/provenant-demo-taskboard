package com.github.hantsy.ee8sample.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceContext;

import com.github.hantsy.ee8sample.domain.LoginToken;
import com.github.hantsy.ee8sample.domain.support.AbstractRepository;
import jakarta.persistence.EntityManager;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LoginTokenRepository extends AbstractRepository<LoginToken, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected EntityManager entityManager() {
        return em;
    }

    public void removeByTokenHash(String tokenHash) {
        this.em.createNamedQuery("LoginToken.remove")
                .setParameter("tokenHash", tokenHash)
                .executeUpdate();
    }
}
