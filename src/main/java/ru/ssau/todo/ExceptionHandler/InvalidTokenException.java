package ru.ssau.todo.ExceptionHandler;

public class InvalidTokenException extends  RuntimeException{
    public InvalidTokenException(String message){
        super(message);
    }
}
