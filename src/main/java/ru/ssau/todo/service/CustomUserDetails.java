package ru.ssau.todo.service;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ssau.todo.entity.User;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private final Collection<CustomGrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<CustomGrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public Long getId() {
        return user.getUserId();
    }

    @Override
    @NullMarked
    public Collection<CustomGrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return user.getUsername();
    }

}
