package ru.ssau.todo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRole = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Task> userTask = new ArrayList<>();

    public User() {
    }

    public User(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public User(Long userId, List<Task> userTask, List<UserRole> userRole, String username) {
        this.userId = userId;
        this.userTask = userTask;
        this.userRole = userRole;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
