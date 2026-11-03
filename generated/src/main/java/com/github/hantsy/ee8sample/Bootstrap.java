package com.github.hantsy.ee8sample;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Transactional(propagation = Propagation.REQUIRED)
public class Bootstrap {

    private final Logger LOG;

    private final TaskRepository taskRepository;

    public Bootstrap(Logger log, TaskRepository taskRepository) {
        this.LOG = log;
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "bootstraping application...");

        Task task = new Task();
        task.setName("My first task");
        task.setDescription("The description of my first task");
        task.setStatus(Task.Status.TODO);

        task = taskRepository.save(task);

        LOG.log(Level.INFO, "inserted task: {0}", new Object[]{task});
    }
}
