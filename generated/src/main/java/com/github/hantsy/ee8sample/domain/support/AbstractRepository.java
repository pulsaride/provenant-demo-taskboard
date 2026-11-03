package com.github.hantsy.ee8sample.domain.support;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class AbstractRepository<T extends AbstractEntity, ID> {

    protected abstract EntityManager entityManager();

    private Class<T> entityClass() {
        @SuppressWarnings("unchecked")
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    public List<T> findAll() {
        CriteriaBuilder cb = this.entityManager().getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass());
        Root<T> c = q.from(entityClass());
        return entityManager().createQuery(q).getResultList();
    }

    public T save(T entity) {
        if (entity.getId() == null) {
            entityManager().persist(entity);

            return entity;
        } else {
            return entityManager().merge(entity);
        }
    }

    public T findById(ID id) {
        return entityManager().find(entityClass(), id);
    }

    public void delete(T entity) {
        T _entity = entityManager().merge(entity);
        entityManager().remove(_entity);
    }

    public void deleteById(ID id) {
        T _entity = findById(id);
        entityManager().remove(_entity);
    }

    public Stream<T> stream() {
        CriteriaBuilder cb = this.entityManager().getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass());
        Root<T> c = q.from(entityClass());
        return entityManager().createQuery(q).getResultStream();
    }

    public Optional<T> findOptionalById(ID id) {
        return Optional.ofNullable(findById(id));
    }
}
