package ru.ssau.todo.entity;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {

    private Long userId;
    private String username;
    private List<Role> role;
    private List<TaskDto> task;
}
