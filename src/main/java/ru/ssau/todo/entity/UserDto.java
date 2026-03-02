package ru.ssau.todo.entity;

import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class UserDto {

    private Long userId;
    private String username;
    private String password;
    private Set<Role> role;
    private List<TaskDto> task;
}
