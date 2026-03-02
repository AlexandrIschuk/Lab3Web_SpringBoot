package ru.ssau.todo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Role;
import ru.ssau.todo.entity.UserDto;
import ru.ssau.todo.entity.UserRole;
import ru.ssau.todo.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;
    MappingUtils mappingUtils;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, MappingUtils mappingUtils, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.mappingUtils = mappingUtils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ru.ssau.todo.entity.User user = userRepository.findUByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(("User not found: " + username)));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRole()))
                .build();

    }

    public void UserRegister(UserDto userDto){
        ru.ssau.todo.entity.User user = mappingUtils.mapToUserEntity(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(user.getUsername().equals("admin")){
            user.setRole(Collections.singleton(new Role(1L, UserRole.ROLE_ADMIN)));
        }
        else {
            user.setRole(Collections.singleton(new Role(2L, UserRole.ROLE_USER)));
        }
        userRepository.save(user);
    }
}
