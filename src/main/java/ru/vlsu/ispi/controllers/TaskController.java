package ru.vlsu.ispi.controllers;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskAccessDeniedException;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.TaskListService;
import ru.vlsu.ispi.services.TaskService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

        // Получаем только задачи, созданные текущим пользователем
        Page<Task> taskPage = taskService.findTasksCreatedByUser(currentUser, pageable);

        List<Task> sortedTasks = taskPage.getContent().stream()
                .sorted((t1, t2) -> {
                    boolean t1Completed = Task.Status.Completed.equals(t1.getStatus());
                    boolean t2Completed = Task.Status.Completed.equals(t2.getStatus());

                    if (t1Completed == t2Completed) {
                        return t1.getDeadlineAt().compareTo(t2.getDeadlineAt());
                    }
                    return Boolean.compare(t1Completed, t2Completed);
                })
                .collect(Collectors.toList());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("tasks", sortedTasks);
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


    @GetMapping("/assigned")
    public String showAssignedTasks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) Task.Status status,
            @RequestParam(value = "search", required = false) String search,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);

        // Получаем задачи с фильтрацией
        Page<Task> assignedTasksPage = taskService.findTasksAssignedToUserWithFilters(
                currentUser, status, search, pageable);

        model.addAttribute("assignedTasks", assignedTasksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", assignedTasksPage.getTotalPages());
        model.addAttribute("totalElements", assignedTasksPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("statuses", Arrays.asList(Task.Status.values()));

        return "assignedTasks";
    }

    @PostMapping("/complete")
    public String completeTask(
            @RequestParam("taskId") int taskId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Task> taskOptional = taskService.getTaskById(taskId);
        if (!taskOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Задача не найдена");
            return "redirect:/tasks/assigned";
        }

        Task task = taskOptional.get();

        // Проверяем, что задача назначена текущему пользователю
        if (task.getAssignedUser() == null || currentUser.getId() != task.getAssignedUser().getId()) {
            redirectAttributes.addFlashAttribute("error", "Вы не можете выполнить эту задачу");
            return "redirect:/tasks/assigned";
        }

        // Выполняем задачу
        task.setStatus(Task.Status.Completed);
        taskService.save(task);

        // НАЧИСЛЕНИЕ БАЛЛОВ ИСПОЛНИТЕЛЮ
        User assignedUser = task.getAssignedUser();
        assignedUser.setTotalPoints(assignedUser.getTotalPoints() + task.getPoints());
        userService.save(assignedUser);

        // Обновляем данные пользователя в текущей сессии, чтобы новые баллы сразу отобразились в интерфейсе
        session.setAttribute("user", assignedUser);

        redirectAttributes.addFlashAttribute("success", "Задача выполнена успешно! Вам начислено " + task.getPoints() + " XP.");
        return "redirect:/tasks/assigned";
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
    public String showTaskForm(@RequestParam(value = "id", required = false) Integer id,
                               Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("user");

        // Принудительно загружаем друзей
        if (currentUser.getFriends() == null) {
            currentUser.setFriends(new ArrayList<>());
        }
        if (currentUser.getAllFriends() == null) {
            currentUser.setAllFriends(new ArrayList<>());
        }

        Task task;
        if (id != null && id > 0) {
            task = taskService.getTaskById(id)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
        } else {
            task = new Task();
            task.setUser(currentUser);
        }

        model.addAttribute("task", task);
        model.addAttribute("currentUser", currentUser);

        List<TaskList> taskLists = taskListService.findByUser(currentUser);
        model.addAttribute("taskLists", taskLists);

        // Формируем список доступных пользователей
        Set<User> availableUsersSet = new HashSet<>();
        availableUsersSet.add(currentUser); // себя
        availableUsersSet.addAll(currentUser.getFriends()); // друзья
        availableUsersSet.addAll(currentUser.getAllFriends()); // те, кто добавил нас

        List<User> availableUsers = new ArrayList<>(availableUsersSet);
        availableUsers.sort(Comparator.comparing(User::getName));

        model.addAttribute("availableUsers", availableUsers);

        model.addAttribute("categories", Arrays.asList(Task.Category.values()));
        model.addAttribute("statuses", Arrays.asList(Task.Status.values()));

        return "taskForm";
    }

    @PostMapping("/add_edit")
    public String addEditTask(@Valid @ModelAttribute("task") Task task,
                              BindingResult result,
                              @RequestParam(value = "assignedUserId", required = false) Integer assignedUserId,
                              Model model, HttpSession session) {

        User currentUser = (User) session.getAttribute("user");

        // Проверка дедлайна: не раньше чем через 5 минут от текущего времени
        if (task.getDeadlineAt() != null) {
            LocalDateTime nowPlus5Minutes = LocalDateTime.now().plusMinutes(5);
            if (task.getDeadlineAt().isBefore(nowPlus5Minutes)) {
                result.rejectValue("deadlineAt", "invalid.deadline",
                        "Дедлайн должен быть не ранее чем через 5 минут от текущего времени");
            }
        }

        // Обработка назначенного пользователя (существующий код)
        if (assignedUserId != null && assignedUserId > 0) {
            User assignedUser = userService.getUserById(assignedUserId)
                    .orElse(null);
            if (!taskService.isValidAssignedUser(currentUser, assignedUser)) {
                result.rejectValue("assignedUserId", "error.assignedUser",
                        "Нельзя назначить задачу пользователю, который не является вами или вашим другом");
            } else {
                task.setAssignedUser(assignedUser);
            }
        } else {
            task.setAssignedUser(currentUser);
        }

        // Обработка taskList (существующий код)
        if (task.getTaskList() != null && task.getTaskList().getId() == -1) {
            task.setTaskList(null);
        }

        if (result.hasErrors()) {
            // Логика обработки ошибок (существующий код)
            List<TaskList> taskLists = taskListService.findByUser(currentUser);
            model.addAttribute("taskLists", taskLists);
            model.addAttribute("categories", Arrays.asList(Task.Category.values()));
            model.addAttribute("statuses", Arrays.asList(Task.Status.values()));

            Set<User> availableUsersSet = new HashSet<>();
            availableUsersSet.add(currentUser);
            if (currentUser.getFriends() != null) availableUsersSet.addAll(currentUser.getFriends());
            if (currentUser.getAllFriends() != null) availableUsersSet.addAll(currentUser.getAllFriends());
            List<User> availableUsers = new ArrayList<>(availableUsersSet);
            availableUsers.sort(Comparator.comparing(User::getName));
            model.addAttribute("availableUsers", availableUsers);
            model.addAttribute("currentUser", currentUser);

            return "taskForm";
        }

        taskService.save(task);
        return "redirect:/tasks";
    }



    @GetMapping("/delete")
    public String deleteTask(@RequestParam("id") int id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            taskService.deleteTask(id, currentUser);
        } catch (TaskAccessDeniedException e) {
            return "redirect:/tasks?error=access_denied";
        }

        return "redirect:/tasks";
    }
}
