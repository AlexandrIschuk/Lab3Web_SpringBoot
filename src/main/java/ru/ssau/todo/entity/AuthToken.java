package ru.ssau.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class AuthToken {
    private String token;
    private String refreshToken;
}
