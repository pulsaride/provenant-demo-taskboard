package com.github.hantsy.ee8sample.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

import jakarta.persistence.PersistenceContext;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.domain.support.AbstractRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TaskRepository extends AbstractRepository<Task, Long> {

    @PersistenceContext
    EntityManager em;

    public List<Task> findByStatus(Task.Status status) {
        Objects.requireNonNull(status, "task status can not be null");
        
        return this.stream()
                .filter(t -> t.getStatus() == status)
                .sorted(Task.DEFAULT_COMPARATOR)
                .collect(toList());
    }

    @Override
    protected EntityManager entityManager() {
        return em;
    }
}
