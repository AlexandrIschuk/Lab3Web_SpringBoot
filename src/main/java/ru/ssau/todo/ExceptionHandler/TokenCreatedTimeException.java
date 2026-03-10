package ru.ssau.todo.ExceptionHandler;

public class TokenCreatedTimeException extends RuntimeException{
    public TokenCreatedTimeException(String message){
        super(message);
    }
}
