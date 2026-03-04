package ru.ssau.todo.ExceptionHandler;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> noSuchElementException(NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> illegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal State Error: " + e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> dataAccessException(DataAccessException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Data Access Error: " + e.getMessage());
    }

    @ExceptionHandler(InvalidKeyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> invalidKeyException(InvalidKeyException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> noSuchAlgorithmException(NoSuchAlgorithmException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> invalidTokenException(InvalidTokenException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
    }
}
