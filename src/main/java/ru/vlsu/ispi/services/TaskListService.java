package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.repositories.TaskListRepository;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;

import java.util.List;
import java.util.Optional;

@Service
public class TaskListService {
    private final TaskListRepository taskListRepository;

    @Autowired
    public TaskListService(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    public TaskList createTaskList(String title, User user, List<Task> tasks) {
        TaskList taskList = new TaskList(title, user, tasks);
        return taskListRepository.save(taskList);
    }

    public Optional<TaskList> getTaskListById(int id) {
        return taskListRepository.findById(id);
    }

    public TaskList updateTaskList(int id, String title,
                                   User user, List<Task> tasks) {
        TaskList taskList = taskListRepository.findById(id).orElseThrow(() -> new RuntimeException("TaskList not found"));
        taskList.setTitle(title);
        taskList.setUser(user);
        taskList.setTasks(tasks);
        return taskListRepository.save(taskList);
    }

    public void deleteTask(int id) {
        taskListRepository.deleteById(id);
    }

    public List<TaskList> getAllTaskLists() {
        return taskListRepository.findAll();
    }

    public List<TaskList> findByUser(User user) {
        return taskListRepository.findByUser(user);
    }
}
