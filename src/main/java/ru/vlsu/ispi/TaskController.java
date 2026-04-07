package ru.vlsu.ispi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskListService taskListService;

    public TaskController(TaskService taskService, UserService userService, TaskListService taskListService) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskListService = taskListService;
    }

    @GetMapping
    public String allTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks";
    }


    @GetMapping({"/add", "/edit"})
    public String showTaskForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Task task;
        if (id != null && id > 0) {
            task = taskService.getTaskById(id)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
        } else {
            task = new Task();
        }
        model.addAttribute("task", task);

        // Получаем всех пользователей для выпадающего списка
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        // Получаем все списки задач для выпадающего списка
        List<TaskList> taskLists = taskListService.getAllTaskLists();
        model.addAttribute("taskLists", taskLists);

        // Добавляем значения enum в модель
        model.addAttribute("categories", Arrays.asList(Task.Category.values()));
        model.addAttribute("statuses", Arrays.asList(Task.Status.values()));

        return "taskForm";
    }

    @PostMapping("/add_edit")
    public String addEditTask(@Valid @ModelAttribute("task") Task task,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "taskForm";
        }

        if (task.getId() == 0) {
            // Создание новой задачи
            taskService.createTask(
                    task.getTitle(),
                    task.getTaskList(),
                    task.getUser(),
                    task.getCategory(),
                    task.getStatus(),
                    task.getDetails(),
                    task.getDeadlineAt(),
                    task.getPoints()
            );
        } else {
            // Обновление существующей задачи
            taskService.updateTask(
                    task.getId(),
                    task.getTitle(),
                    task.getTaskList(),
                    task.getUser(),
                    task.getCategory(),
                    task.getStatus(),
                    task.getDetails(),
                    task.getDeadlineAt(),
                    task.getPoints()
            );
        }

        return "redirect:/tasks";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam("id") int id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
