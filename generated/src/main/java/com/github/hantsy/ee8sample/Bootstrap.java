package com.github.hantsy.ee8sample;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.repository.TaskRepository;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Profile("!legacydb")
public class Bootstrap implements SmartInitializingSingleton {

    private final Logger LOG;

    private final TaskRepository taskRepository;

    public Bootstrap(Logger log, TaskRepository taskRepository) {
        this.LOG = log;
        this.taskRepository = taskRepository;
    }

    public void init() {
        LOG.log(Level.INFO, "bootstraping application...");

        Task task = new Task();
        task.setName("My first task");
        task.setDescription("The description of my first task");
        task.setStatus(Task.Status.TODO);

        task = taskRepository.save(task);

        LOG.log(Level.INFO, "inserted task: {0}", new Object[]{task});
    }

    private TransactionTemplate transactionTemplate;

    @Autowired
    void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void afterSingletonsInstantiated() {
        transactionTemplate.executeWithoutResult(status -> init());
    }
}
