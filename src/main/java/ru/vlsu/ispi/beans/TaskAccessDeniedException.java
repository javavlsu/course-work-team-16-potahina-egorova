package ru.vlsu.ispi.beans;

public class TaskAccessDeniedException extends RuntimeException {
    public TaskAccessDeniedException(String message) {
        super(message);
    }
}
