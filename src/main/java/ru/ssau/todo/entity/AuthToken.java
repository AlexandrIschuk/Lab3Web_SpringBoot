package ru.ssau.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthToken {
    private String token;
}
