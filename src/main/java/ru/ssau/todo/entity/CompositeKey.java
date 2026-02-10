package ru.ssau.todo.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;



@Embeddable
public class CompositeKey implements Serializable {
    private Long userId;
    private Long roleId;

    public CompositeKey(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public CompositeKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
