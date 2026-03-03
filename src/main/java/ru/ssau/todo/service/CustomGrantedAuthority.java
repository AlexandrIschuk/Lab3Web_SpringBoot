package ru.ssau.todo.service;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {

    @Getter
    private final Long roleId;
    private final String roleName;

    public CustomGrantedAuthority(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    @Override
    public @Nullable String getAuthority() {
        return roleName;
    }

}
