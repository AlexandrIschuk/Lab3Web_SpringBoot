package ru.ssau.todo.ExceptionHandler;

public class RefreshTokenException extends RuntimeException{
    public RefreshTokenException(String message){
        super(message);
    }
}
