package ru.ssau.todo.entity;

import jakarta.persistence.Entity;

public enum TaskStatus{
    OPEN,
    DONE,
    IN_PROGRESS,
    CLOSED
}
