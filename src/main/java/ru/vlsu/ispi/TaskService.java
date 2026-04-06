package ru.vlsu.ispi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.beans.Task.Category;
import ru.vlsu.ispi.beans.Task.Status;

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

    public Task createTask(String title, TaskList taskList,
                           User user, Category category, Status status,
                           String details, LocalDateTime deadlineAt,
                           int points) {
        Task task = new Task(title, taskList, user, category, status,
                details, deadlineAt, points);
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(int id, String title, TaskList taskList,
                           User user, Category category, Status status,
                           String details, LocalDateTime deadlineAt,
                           int points) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(title);
        task.setTaskList(taskList);
        task.setUser(user);
        task.setCategory(category);
        task.setStatus(status);
        task.setDetails(details);
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
}
