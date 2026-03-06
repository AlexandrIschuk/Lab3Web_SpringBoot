package ru.ssau.todo.ExceptionHandler;

import javax.naming.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String message){
        super(message);
    }
}
