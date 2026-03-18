package ru.ssau.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Set;

@Data
@JsonIgnoreProperties(value = {"password", "userId"}, allowSetters = true)
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private Set<Role> role;

}