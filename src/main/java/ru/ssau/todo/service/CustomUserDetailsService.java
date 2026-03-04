package ru.ssau.todo.service;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Role;
import ru.ssau.todo.entity.User;
import ru.ssau.todo.entity.UserDto;
import ru.ssau.todo.entity.UserRole;
import ru.ssau.todo.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;
    MappingUtils mappingUtils;
    BCryptPasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, MappingUtils mappingUtils, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.mappingUtils = mappingUtils;
        this.passwordEncoder = bCryptPasswordEncoder;
    }


    @Override
    @NullMarked
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(("User not found: " + username)));

        Set<CustomGrantedAuthority> roles = user.getRole().stream()
                .map(role -> new CustomGrantedAuthority(role.getRoleId(),role.getRoleName().name()))
                .collect(Collectors.toSet());
        return new CustomUserDetails(user,roles);
    }

    public CustomUserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(("User not found: " + userId)));

        Set<CustomGrantedAuthority> roles = user.getRole().stream()
                .map(role -> new CustomGrantedAuthority(role.getRoleId(),role.getRoleName().name()))
                .collect(Collectors.toSet());
        return new CustomUserDetails(user,roles);
    }

    public void UserRegister(UserDto userDto){
        User user = mappingUtils.mapToUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getUsername().equals("admin")){
            user.setRole(Collections.singleton(new Role(1L, UserRole.ROLE_ADMIN)));
        }
        else {
            user.setRole(Collections.singleton(new Role(2L, UserRole.ROLE_USER)));
        }
        userRepository.save(user);
    }

}
