package ru.ssau.todo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "userrole")
public class UserRole {
    @EmbeddedId
    private CompositeKey id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    private Role role;

    public UserRole() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
