package ru.ssau.todo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssau.todo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.entity.TaskDto;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task){
        TaskDto task1 = taskService.create(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task1.getId())
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(task1, headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable long id) {

        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<TaskDto> getTasks(@RequestParam(required = false) LocalDateTime from, @RequestParam(required = false) LocalDateTime to, @RequestParam Long userId) {
        return taskService.findAll(from, to, userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<TaskDto>> updateTask(@PathVariable long id, @RequestBody TaskDto task) {
        task.setId(id);
        taskService.update(task);
        return ResponseEntity.ok(taskService.findById(id));

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
