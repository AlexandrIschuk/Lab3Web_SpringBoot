package ru.ssau.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ssau.todo.entity.*;
import ru.ssau.todo.service.CustomUserDetails;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<UserDto> getAuthUser(@AuthenticationPrincipal CustomUserDetails user) {
        UserDto userDto = new UserDto();
        Set<Role> roles = user.getAuthorities().stream()
                .map(role -> new Role(role.getRoleId(),UserRole.valueOf(role.getAuthority())))
                .collect(Collectors.toSet());
        userDto.setUsername(user.getUsername());
        userDto.setRole(roles);
        return ResponseEntity.ok(userDto);
    }
}
