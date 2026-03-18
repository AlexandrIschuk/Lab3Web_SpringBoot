package ru.ssau.todo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssau.todo.service.CustomUserDetails;
import ru.ssau.todo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.entity.TaskDto;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task, @AuthenticationPrincipal CustomUserDetails userDetails) {
        task.setCreatedBy(userDetails.getId());
        task = taskService.create(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(task, headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable long id) {
            return taskService.findById(id).orElseThrow();
    }

    @GetMapping
    public List<TaskDto> getTasks(@RequestParam(required = false) LocalDateTime from, @RequestParam(required = false) LocalDateTime to, @RequestParam(required = false) Long userId) {

        return taskService.findAll(from, to, userId);

    }

    @GetMapping("/stat")
    public List<Object[]> getStatusStat() {
        return taskService.findStatusCount();
    }

    @PutMapping("/{id}")
    public Optional<TaskDto> updateTask(@PathVariable long id, @RequestBody TaskDto task) {
        task.setId(id);
        taskService.update(task);
        return taskService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long id) {
        taskService.deleteById(id);
    }

    @GetMapping("/active/count")
    public Map<String, Long> countActiveTask(@RequestParam long userId) {
        long count = taskService.countActiveTasksByUserId(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("activeTasksCount", count);
        return response;
    }
}
