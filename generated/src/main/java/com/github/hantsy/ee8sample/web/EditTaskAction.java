package com.github.hantsy.ee8sample.web;

import org.springframework.web.context.annotation.RequestScope;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.service.TaskService;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "editTaskAction")
@RequestScope
public class EditTaskAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(EditTaskAction.class.getName());

    private final TaskService taskService;

    private Long taskId;

    private Task task;

    public EditTaskAction(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task getTask() {
        return task;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void init() {
        LOG.log(Level.INFO, " get task of id @{0}", taskId);
        if (taskId == null) {
            task = new Task();
        } else {
            task = taskService.findById(taskId);
        }
    }

    public String save() {
        LOG.log(Level.INFO, "saving task@{0}", task);
        if (this.task.getId() == null) {
            this.task = taskService.save(task);
        } else {
            this.task = taskService.update(taskId, task);
        }

        return "/tasks.xhtml?faces-redirect=true";
    }
}
