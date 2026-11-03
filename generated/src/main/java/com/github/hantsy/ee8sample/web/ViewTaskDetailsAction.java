package com.github.hantsy.ee8sample.web;

import org.springframework.web.context.annotation.RequestScope;

import jakarta.validation.constraints.NotNull;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.repository.TaskRepository;
import com.github.hantsy.ee8sample.service.TaskNotFoundException;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "viewTaskAction")
@RequestScope
public class ViewTaskDetailsAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(ViewTaskDetailsAction.class.getName());

    private final TaskRepository taskRepository;

    @NotNull
    private Long taskId;

    private Task task;

    public ViewTaskDetailsAction(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void init() {
        log.log(Level.INFO, " get task of id @" + taskId);

        task = taskRepository.findById(taskId);

        if (task == null) {
            throw new TaskNotFoundException(taskId);
        }
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Task getTask() {
        return task;
    }
}
