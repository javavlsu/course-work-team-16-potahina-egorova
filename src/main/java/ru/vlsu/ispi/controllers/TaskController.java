package ru.vlsu.ispi.controllers;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.TaskListService;
import ru.vlsu.ispi.services.TaskService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

//    @GetMapping
//    public String allTasks(HttpSession session, Model model) {
//        if (session.getAttribute("user") == null) {
//            return "redirect:/login";
//        }
//        List<Task> tasks = taskService.getAllTasks();
//        model.addAttribute("tasks", tasks);
//        return "tasks";
//    }

    @GetMapping
    public String showUserTasks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "category", required = false) Task.Category category,
            @RequestParam(value = "status", required = false) Task.Status status,
            @RequestParam(value = "search", required = false) String search,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.findTasksByUserWithFilters(
                currentUser, category, status, search, pageable
        );

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("totalElements", taskPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("categories", Arrays.asList(Task.Category.values()));
        model.addAttribute("statuses", Arrays.asList(Task.Status.values()));

        return "tasks";
    }


    @GetMapping("/details")
    public String showTaskDetails(
            @RequestParam("id") int taskId,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Task> taskOptional = taskService.getTaskById(taskId);
        if (!taskOptional.isPresent()) {
            model.addAttribute("error", "Задача не найдена");
            return "error"; // или перенаправление на страницу с ошибкой
        }

        Task task = taskOptional.get();

        // Принудительно загружаем связанные объекты
        if (task.getUser() != null) {
            Hibernate.initialize(task.getUser());
        }
        if (task.getTaskList() != null) {
            Hibernate.initialize(task.getTaskList());
        }

        model.addAttribute("task", task);
        return "taskDetails";
    }

    @GetMapping({"/add", "/edit"})
    public String showTaskForm(@RequestParam(value = "id", required = false) Integer id, Model model,
                               HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Task task;
        if (id != null && id > 0) {
            task = taskService.getTaskById(id)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            // Принудительно загружаем связанные объекты
            if (task.getUser() != null) {
                Hibernate.initialize(task.getUser());
            }
            if (task.getTaskList() != null) {
                Hibernate.initialize(task.getTaskList());
            }
        } else {
            task = new Task();
        }
        model.addAttribute("task", task);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        List<TaskList> taskLists = taskListService.getAllTaskLists();
        model.addAttribute("taskLists", taskLists);

        model.addAttribute("categories", Arrays.asList(Task.Category.values()));
        model.addAttribute("statuses", Arrays.asList(Task.Status.values()));

        return "taskForm";
    }

    @PostMapping("/add_edit")
    public String addEditTask(@Valid @ModelAttribute("task") Task task,
                              BindingResult result,
                              Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "taskForm";
        }

        if (task.getId() == 0) {
            // Создание новой задачи
            taskService.createTask(
                    task.getTitle(),
                    task.getTaskList(),
                    task.getUser(),
                    task.getAssignedUser(),
                    task.getCategory(),
                    task.getStatus(),
                    task.getDetails(),
                    task.getAssignedAt(),
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
                    task.getAssignedUser(),
                    task.getCategory(),
                    task.getStatus(),
                    task.getDetails(),
                    task.getAssignedAt(),
                    task.getDeadlineAt(),
                    task.getPoints()
            );
        }

        return "redirect:/tasks";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
