package com.github.hantsy.ee8sample.domain;

import static com.github.hantsy.ee8sample.domain.Task.Status.TODO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.github.hantsy.ee8sample.domain.support.AbstractAuditableEntity;
import jakarta.persistence.EnumType;
import java.util.Comparator;
import java.util.function.Function;
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
public class Task extends AbstractAuditableEntity {

    private static final long serialVersionUID = 1L;

    public static Comparator<Task> DEFAULT_COMPARATOR = Comparator
            .comparing(Task::getCreatedDate).reversed()
            .thenComparing(Task::getName)
            .thenComparing(Task::getDescription);

    public static Function<Task, String> TO_STRING = t
            -> "Task[name:" + t.getName()
            + "\n description:" + t.getDescription()
            + "\n status:" + t.getStatus()
            + "\n createdAt:" + t.getCreatedDate()
            + "\n lastModifiedAt:" + t.getLastModifiedDate()
            + "]";

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status = TODO;

    public enum Status {
        TODO,
        DOING,
        DONE,
        ;
    }
}
