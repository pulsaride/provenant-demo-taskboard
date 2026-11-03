package com.github.hantsy.ee8sample.service;

import org.springframework.stereotype.Component;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.repository.TaskRepository;
import java.util.List;

@Component
public class TaskService {

    private final TaskRepository tasks;

    public TaskService(TaskRepository tasks) {
        this.tasks = tasks;
    }

    public Task findById(Long id) {
        return this.tasks.findOptionalById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> findByStatus(Task.Status status) {
        return this.tasks.findByStatus(status);
    }

    public Task save(Task data) {
        return this.tasks.save(data);
    }

    public Task update(Long id, Task data) {
        Task _existed = findById(id);
        _existed.setName(data.getName());
        _existed.setDescription(data.getDescription());
        return this.tasks.save(_existed);
    }

    public void deleteById(Long id) {
        Task _existed = findById(id);
        this.tasks.delete(_existed);
    }

    public void updateStatus(Long id, Task.Status status) {
        Task _existed = findById(id);
        _existed.setStatus(status);
        this.tasks.save(_existed);
    }
}
