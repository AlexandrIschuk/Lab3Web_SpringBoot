package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskDto;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.entity.UserDto;

@Service
public class MappingUtils {
    public TaskDto mapToTaskDto(Task entity){
        TaskDto dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setStatus(entity.getStatus());
        dto.setCreatedBy(entity.getCreatedBy().getUserId());
        dto.setCreatedAt(entity.getCreatedAt());
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
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public User mapToUserEntity(UserDto dto){
        User entity = new User();
        entity.setUserId(dto.getUserId());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        return entity;
    }

}
