package ru.ssau.todo.Service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskDto;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.entity.UserDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingUtils {
    public TaskDto mapToTaskDto(Task entity){
        TaskDto dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setStatus(entity.getStatus());
        dto.setCreatedBy(entity.getCreatedBy().getUserId());
        dto.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return dto;
    }
    public Task mapToTaskEntity(TaskDto dto){
        Task entity = new Task();
        User user = new User();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setStatus(dto.getStatus());
        user.setUserId(dto.getCreatedBy());
        entity.setCreatedBy(user);
        entity.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return entity;
    }

    public User mapToUserEntity(UserDto dto){
        User entity = new User();
        entity.setUserId(dto.getUserId());
        entity.setUsername(dto.getUsername());
        entity.setRole(dto.getRole());
        return entity;
    }
    public UserDto mapToUserDto(User dto){
        UserDto entity = new UserDto();
        entity.setUserId(dto.getUserId());
        entity.setUsername(dto.getUsername());
        entity.setRole(dto.getRole());
        List<Task> tasks = dto.getTask();
        List<TaskDto> tasksDto = tasks.stream()
                .map(this::mapToTaskDto)
                .collect(Collectors.toList());
        entity.setTask(tasksDto);
        return entity;
    }
}
