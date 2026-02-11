package ru.ssau.todo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskDto;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    MappingUtils mappingUtils = new MappingUtils();

    public TaskDto create(TaskDto task){
        if(countActiveTasksByUserId(task.getCreatedBy()) >= 10){
            throw new IllegalStateException(
                    String.format(
                            "User with id %d cannot have more than 10 active tasks. Current count: %d",
                            task.getCreatedBy(),
                            countActiveTasksByUserId(task.getCreatedBy())
                    )
            );
        }
       return mappingUtils.mapToTaskDto(taskRepository.save(mappingUtils.mapToTaskEntity(task)));
    }

    public Optional<TaskDto> findById(long id) {
        return Optional.ofNullable(mappingUtils.mapToTaskDto(taskRepository.findById(id).orElseThrow()));
    }

    public List<TaskDto> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        List<Task> tasks = taskRepository.findAllFilter(from, to, userId);
        return tasks.stream()
                .map(mappingUtils::mapToTaskDto)
                .collect(Collectors.toList());

    }

    public void update(Task task) throws Exception {
        Task task1 = taskRepository.findById(task.getId()).orElseThrow();
        task1.setTitle(task.getTitle());
        task1.setStatus(task.getStatus());
        taskRepository.save(task1);
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
