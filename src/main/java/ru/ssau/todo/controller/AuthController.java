package ru.ssau.todo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.entity.*;
import ru.ssau.todo.service.CustomUserDetails;
import ru.ssau.todo.service.CustomUserDetailsService;
import ru.ssau.todo.service.TokenService;

import javax.naming.AuthenticationException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, CustomUserDetailsService customUserDetailsService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

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

    @PostMapping("/login")
    public ResponseEntity<AuthToken> jwtLogin(@RequestBody UserDto user) throws NoSuchAlgorithmException, InvalidKeyException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        ));
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String refToken = tokenService.generateRefreshToken(userDetails);
        ResponseCookie cookie = ResponseCookie.from("REFRESH_TOKEN", refToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .domain("localhost")
                .build();
        AuthToken authToken = new AuthToken(tokenService.generateToken(userDetails));
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(authToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthToken> refreshToken(@CookieValue(value = "REFRESH_TOKEN") String refreshToken) throws NoSuchAlgorithmException, InvalidKeyException, AuthenticationException{
        Map<String,Object> payload = tokenService.getDecodePayload(refreshToken);
        long userId = Long.parseLong(payload.get("userId").toString());
        CustomUserDetails userDetails = userDetailsService.loadUserByUserId(userId);
        AuthToken authToken = new AuthToken(tokenService.generateToken(userDetails));
        return ResponseEntity.ok(authToken);
    }

}
