package ru.ssau.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.service.CustomUserDetailsService;
import ru.ssau.todo.entity.UserDto;

@Controller
@RequestMapping("/users")
public class UserController {
    CustomUserDetailsService customUserDetailsService;

    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void UserRegister(@RequestBody UserDto userDto){
        customUserDetailsService.UserRegister(userDto);
    }
}
