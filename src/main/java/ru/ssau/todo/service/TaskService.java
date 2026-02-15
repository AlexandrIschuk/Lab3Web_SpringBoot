package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskDto;
import ru.ssau.todo.entity.TaskStatus;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private int activeTasks = 10;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    MappingUtils mappingUtils = new MappingUtils();

    private void validateCountOfActiveTasks(TaskDto task) {
        long l = countActiveTasksByUserId(task.getCreatedBy());
        if (l >= activeTasks) {
            throw new IllegalStateException(String.format("User with id %d cannot have more than %d active tasks. Current count: %d", task.getCreatedBy(), activeTasks,l));
        }
    }

    public TaskDto create(TaskDto task){
        validateCountOfActiveTasks(task);
        return mappingUtils.mapToTaskDto(taskRepository.save(mappingUtils.mapToTaskEntity(task)));
    }

    public Optional<TaskDto> findById(long id) {
        return Optional.ofNullable(mappingUtils.mapToTaskDto(taskRepository.findById(id).orElseThrow()));
    }

    public List<TaskDto> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        if(from == null){
            from = LocalDateTime.of(1970, 1,1,0,0);
        }
        if(to == null) {
            to = LocalDateTime.now();
        }
        List<Task> tasks = taskRepository.findAllFilter(from, to, userId);
        return tasks.stream()
                .map(mappingUtils::mapToTaskDto)
                .collect(Collectors.toList());

    }
    public void taskUpdate(TaskDto task, Task task1){
        task1.setTitle(task.getTitle());
        task1.setStatus(task.getStatus());
        taskRepository.save(task1);
    }

    public void update(TaskDto task){
        Task task1 = taskRepository.findById(task.getId()).orElseThrow();
        if ((task.getStatus() == TaskStatus.DONE || task.getStatus() == TaskStatus.CLOSED) || ((task1.getStatus() == TaskStatus.IN_PROGRESS && task.getStatus() == TaskStatus.OPEN) || (task1.getStatus() == TaskStatus.OPEN && task.getStatus() == TaskStatus.IN_PROGRESS))) {
            taskUpdate(task,task1);
        } else {
            validateCountOfActiveTasks(mappingUtils.mapToTaskDto(task1));
            taskUpdate(task,task1);
        }
    }

    public void deleteById(long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        if(task.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5))){
            throw new IllegalStateException("The task was created less than 5 minutes ago.");
        }
        taskRepository.deleteById(id);

    }
    public long countActiveTasksByUserId(long userId) {
        User user = new User();
        user.setUserId(userId);
         return taskRepository.countActiveTasksByUserId(user);
    }
}
