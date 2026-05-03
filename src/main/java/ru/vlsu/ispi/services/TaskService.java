package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.beans.Task.Category;
import ru.vlsu.ispi.beans.Task.Status;
import ru.vlsu.ispi.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title, TaskList taskList, User creator,
                           User assignedUser, Task.Category category,
                           Task.Status status, String details,
                           LocalDateTime assignedAt, LocalDateTime deadlineAt,
                           int points) {

        if (!isValidAssignedUser(creator, assignedUser)) {
            throw new IllegalArgumentException(
                    ("Нельзя назначить задачу пользователю, который не является вами или вашим другом"));
        }

        Task task = new Task();
        task.setTitle(title);
        task.setTaskList(taskList);
        task.setUser(creator);
        task.setAssignedUser(assignedUser);
        task.setCategory(category);
        task.setStatus(status);
        task.setDetails(details);
        task.setAssignedAt(assignedAt);
        task.setDeadlineAt(deadlineAt);
        task.setPoints(points);

        return taskRepository.save(task);
    }

    public boolean isValidAssignedUser(User creator, User assignedUser) {
        if (assignedUser == null) return true; // необязательно назначать
        if (creator.getId() == assignedUser.getId()) return true; // можно назначить себе
        return creator.getFriends().contains(assignedUser) ||
                creator.getAllFriends().contains(assignedUser);
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(int id, String title, TaskList taskList,
                           User user, User assignedUser, Category category, Status status,
                           String details, LocalDateTime assignedAt, LocalDateTime deadlineAt,
                           int points) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(title);
        task.setTaskList(taskList);
        task.setUser(user);
        task.setAssignedUser(assignedUser);
        task.setCategory(category);
        task.setStatus(status);
        task.setDetails(details);
        task.setAssignedAt(assignedAt);
        task.setDeadlineAt(deadlineAt);
        task.setPoints(points);

        if (task.getCategory() == null) {
            task.setCategory(Category.Other);
        }
        if (task.getStatus() == null) {
            task.setStatus(Status.NotStarted);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        // Валидация обязательных полей
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Заголовок задачи не может быть пустым");
        }
        if (task.getUser() == null) {
            throw new IllegalArgumentException("Автор задачи не указан");
        }

        // Установка значений по умолчанию
        if (task.getCategory() == null) {
            task.setCategory(Category.Other);
        }
        if (task.getStatus() == null) {
            task.setStatus(Status.NotStarted);
        }
        if (task.getAssignedAt() == null) {
            task.setAssignedAt(LocalDateTime.now());
        }

        return taskRepository.save(task);
    }

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    public Page<Task> findTasksByUserWithFilters(
            User user,
            Task.Category category,
            Task.Status status,
            String search,
            Pageable pageable) {

        return taskRepository.findByUserAndFilters(user, category, status, search, pageable);
    }
}
