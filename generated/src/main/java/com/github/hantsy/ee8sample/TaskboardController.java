package com.github.hantsy.ee8sample;

import com.github.hantsy.ee8sample.domain.Task;
import com.github.hantsy.ee8sample.domain.Task.Status;
import com.github.hantsy.ee8sample.web.EditTaskAction;
import com.github.hantsy.ee8sample.web.TaskDetails;
import com.github.hantsy.ee8sample.web.TaskHome;
import com.github.hantsy.ee8sample.web.ViewTaskDetailsAction;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskboardController {

    private final ObjectProvider<EditTaskAction> editTaskActionProvider;
    private final ObjectProvider<TaskHome> taskHomeProvider;
    private final ObjectProvider<ViewTaskDetailsAction> viewTaskDetailsActionProvider;

    public TaskboardController(ObjectProvider<EditTaskAction> editTaskActionProvider, ObjectProvider<TaskHome> taskHomeProvider, ObjectProvider<ViewTaskDetailsAction> viewTaskDetailsActionProvider) {
        this.editTaskActionProvider = editTaskActionProvider;
        this.taskHomeProvider = taskHomeProvider;
        this.viewTaskDetailsActionProvider = viewTaskDetailsActionProvider;
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.ViewTaskDetailsAction (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @GetMapping("/api/viewTaskAction/task")
    public ResponseEntity<ViewTaskActionTaskView> apiViewtaskactionTask(@RequestParam(name = "id", required = false) Long id) {
        ViewTaskDetailsAction bean = viewTaskDetailsActionProvider.getObject();
        if (id != null) { bean.setTaskId(id); }
        bean.init();
        return ResponseEntity.ok(toViewTaskActionTaskView(bean.getTask()));
    }

    // exactly the properties the open legacy pages render (never more open than the legacy)
    public record ViewTaskActionTaskView(LocalDateTime createdDate, String description, LocalDateTime lastModifiedDate, String name, Status status) {}

    private static ViewTaskActionTaskView toViewTaskActionTaskView(Task source) {
        return source == null ? null : new ViewTaskActionTaskView(source.getCreatedDate(), source.getDescription(), source.getLastModifiedDate(), source.getName(), source.getStatus());
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.TaskHome (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @GetMapping("/api/taskHome/donetasks")
    public ResponseEntity<List<TaskHomeDonetasksView>> apiTaskhomeDonetasks() {
        TaskHome bean = taskHomeProvider.getObject();
        bean.init();
        List<TaskDetails> legacyItems = bean.getDonetasks();
        return ResponseEntity.ok(legacyItems == null ? null : legacyItems.stream().map(legacyItem -> toTaskHomeDonetasksView(legacyItem)).toList());
    }

    // exactly the properties the open legacy pages render (never more open than the legacy)
    public record TaskHomeDonetasksView(String description, Long id, String name) {}

    private static TaskHomeDonetasksView toTaskHomeDonetasksView(TaskDetails source) {
        return source == null ? null : new TaskHomeDonetasksView(source.getDescription(), source.getId(), source.getName());
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.TaskHome (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @GetMapping("/api/taskHome/doingtasks")
    public ResponseEntity<List<TaskHomeDoingtasksView>> apiTaskhomeDoingtasks() {
        TaskHome bean = taskHomeProvider.getObject();
        bean.init();
        List<TaskDetails> legacyItems = bean.getDoingtasks();
        return ResponseEntity.ok(legacyItems == null ? null : legacyItems.stream().map(legacyItem -> toTaskHomeDoingtasksView(legacyItem)).toList());
    }

    // exactly the properties the open legacy pages render (never more open than the legacy)
    public record TaskHomeDoingtasksView(String description, Long id, String name) {}

    private static TaskHomeDoingtasksView toTaskHomeDoingtasksView(TaskDetails source) {
        return source == null ? null : new TaskHomeDoingtasksView(source.getDescription(), source.getId(), source.getName());
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.TaskHome (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @PostMapping("/api/taskHome/markTaskDoing")
    public ResponseEntity<Void> apiTaskhomeMarktaskdoing(@RequestParam(name = "id", required = true) Long id) {
        TaskHome bean = taskHomeProvider.getObject();
        bean.markTaskDoing(id);
        return ResponseEntity.noContent().build();
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.TaskHome (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @PostMapping("/api/taskHome/markTaskDone")
    public ResponseEntity<Void> apiTaskhomeMarktaskdone(@RequestParam(name = "id", required = true) Long id) {
        TaskHome bean = taskHomeProvider.getObject();
        bean.markTaskDone(id);
        return ResponseEntity.noContent().build();
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.TaskHome (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @GetMapping("/api/taskHome/todotasks")
    public ResponseEntity<List<TaskHomeTodotasksView>> apiTaskhomeTodotasks() {
        TaskHome bean = taskHomeProvider.getObject();
        bean.init();
        List<TaskDetails> legacyItems = bean.getTodotasks();
        return ResponseEntity.ok(legacyItems == null ? null : legacyItems.stream().map(legacyItem -> toTaskHomeTodotasksView(legacyItem)).toList());
    }

    // exactly the properties the open legacy pages render (never more open than the legacy)
    public record TaskHomeTodotasksView(String description, Long id, String name) {}

    private static TaskHomeTodotasksView toTaskHomeTodotasksView(TaskDetails source) {
        return source == null ? null : new TaskHomeTodotasksView(source.getDescription(), source.getId(), source.getName());
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.TaskHome (one Spring-managed instance per call; legacy scope @ViewScoped)
    @PermitAll
    @PostMapping("/api/taskHome/deleteTask")
    public ResponseEntity<List<FacesGlobalMessage>> apiTaskhomeDeletetask(@RequestParam(name = "id", required = true) Long id) {
        TaskHome bean = taskHomeProvider.getObject();
        bean.deleteTask(id);
        return ResponseEntity.ok(List.of(new FacesGlobalMessage("WARN", "Task is deleted!")));
    }

    /** A JSF global message the page command queued (severity code and literal summary). */
    public record FacesGlobalMessage(String severity, String summary) {}

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.EditTaskAction (one Spring-managed instance per call; legacy scope @ViewScoped)
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/api/editTaskAction/task")
    public ResponseEntity<Task> apiEdittaskactionTask(@RequestParam(name = "id", required = false) Long id) {
        EditTaskAction bean = editTaskActionProvider.getObject();
        if (id != null) { bean.setTaskId(id); }
        bean.init();
        return ResponseEntity.ok(bean.getTask());
    }

    // delegates to the retained backing bean com.github.hantsy.ee8sample.web.EditTaskAction (one Spring-managed instance per call; legacy scope @ViewScoped)
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/api/editTaskAction/save")
    public ResponseEntity<List<FacesGlobalMessage>> apiEdittaskactionSave(@RequestParam(name = "id", required = false) Long id, @RequestBody Task body) {
        EditTaskAction bean = editTaskActionProvider.getObject();
        if (id != null) { bean.setTaskId(id); }
        bean.setTask(body);
        bean.save();
        return ResponseEntity.ok(List.of(new FacesGlobalMessage("INFO", "Task is saved successfully!")));
    }
}
