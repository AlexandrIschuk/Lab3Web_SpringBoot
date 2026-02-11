package ru.ssau.todo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ssau.todo.Service.UserService;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.entity.UserDto;

@Controller
@RequestMapping("/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
