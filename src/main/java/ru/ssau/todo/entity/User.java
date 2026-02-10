package ru.ssau.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username")
    private String username;

//    @OneToMany(cascade = CascadeType.ALL)
//
//    private List<UserRole> userRole = new ArrayList<>();
//
//    @OneToMany(mappedBy = "createdBy")
//    private List<Task> userTask = new ArrayList<>();

}
