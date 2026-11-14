package com.github.hantsy.ee8sample.web;

import org.springframework.web.context.annotation.RequestScope;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.service.TaskService;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "taskHome")
@RequestScope
public class TaskHome implements Serializable {

    private static final Logger log = Logger.getLogger(TaskHome.class.getName());

    private final TaskService taskService;

    private List<TaskDetails> todotasks = new ArrayList<>();

    private List<TaskDetails> doingtasks = new ArrayList<>();

    private List<TaskDetails> donetasks = new ArrayList<>();

    public TaskHome(TaskService taskService) {
        this.taskService = taskService;
    }

    public List<TaskDetails> getTodotasks() {
        return todotasks;
    }

    public List<TaskDetails> getDoingtasks() {
        return doingtasks;
    }

    public List<TaskDetails> getDonetasks() {
        return donetasks;
    }

    public void init() {
        log.log(Level.INFO, "initalizing TaskHome...");
        retrieveAllTasks();
    }

    private void retrieveAllTasks() {
        log.log(Level.INFO, "retriveing all tasks...");
        this.todotasks = findTasksByStatus(Task.Status.TODO);
        this.doingtasks = findTasksByStatus(Task.Status.DOING);
        this.donetasks = findTasksByStatus(Task.Status.DONE);
    }

    private List<TaskDetails> findTasksByStatus(Task.Status status) {
        List<TaskDetails> taskList = new ArrayList<>();
        List<Task> tasks = taskService.findByStatus(status);

        tasks.stream().map((task) -> {
            TaskDetails details = new TaskDetails();
            details.setId(task.getId());
            details.setName(task.getName());
            details.setDescription(task.getDescription());
            details.setCreatedDate(task.getCreatedDate());
            details.setLastModifiedDate(task.getLastModifiedDate());
            return details;
        }).forEach((details) -> {
            taskList.add(details);
        });

        return taskList;
    }

    public void deleteTask(Long id) {
        log.log(Level.INFO, "delete task of id@{0}", id);

        Task task = taskService.findById(id);
        taskService.deleteById(id);

        // retrieve all tasks
        retrieveAllTasks();
    }

    public void markTaskDoing(Long id) {
        log.log(Level.INFO, "changing task DONG @{0}", id);

        taskService.updateStatus(id, Task.Status.DOING);

        // retrieve all tasks
        retrieveAllTasks();
    }

    public void markTaskDone(Long id) {
        log.log(Level.INFO, "changing task DONE @{0}", id);


        taskService.updateStatus(id, Task.Status.DONE);

        // retrieve all tasks
        retrieveAllTasks();
    }
}
