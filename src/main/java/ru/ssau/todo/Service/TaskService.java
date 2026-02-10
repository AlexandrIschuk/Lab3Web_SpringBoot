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

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    MappingUtils mappingUtils = new MappingUtils();

    public Task create(TaskDto task){
        if(countActiveTasksByUserId(task.getCreatedBy()) >= 10){
            throw new IllegalStateException(
                    String.format(
                            "User with id %d cannot have more than 10 active tasks. Current count: %d",
                            task.getCreatedBy(),
                            countActiveTasksByUserId(task.getCreatedBy())
                    )
            );
        }
       return taskRepository.save(mappingUtils.mapToTaskEntity(task));
    }

    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        return taskRepository.findAllFilter(from,to,userId);

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
