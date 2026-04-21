package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.services.AchievementService;
import ru.vlsu.ispi.services.TaskService;
import ru.vlsu.ispi.services.UserAchievementService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/userAchievements")
public class UserAchievementController {
    private final UserAchievementService userAchievementService;
    private final TaskService taskService;
    private final UserService userService;
    private final AchievementService achievementService;

    public UserAchievementController(UserAchievementService userAchievementService,
                                     UserService userService, TaskService taskService,
                                     AchievementService achievementService) {
        this.userAchievementService = userAchievementService;
        this.taskService = taskService;
        this.userService = userService;
        this.achievementService = achievementService;
    }

    @GetMapping
    public String allUserAchievements(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<UserAchievement> userAchievements = userAchievementService.getAllUserAchievements();
        model.addAttribute("userAchievements", userAchievements);
        return "userAchievements";
    }

    @GetMapping({"/add", "/edit"})
    public String showUserAchievementForm(@RequestParam(value = "id", required = false) Integer id,
                                          Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        UserAchievement userAchievement;
        if (id != null && id > 0) {
            userAchievement = userAchievementService.getUserAchievementById(id)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
        } else {
            userAchievement = new UserAchievement();
        }
        model.addAttribute("userAchievement", userAchievement);

        // Получаем всех пользователей для выпадающего списка
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        // Получаем все задачи для выпадающего списка
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);

        List<Achievement> achievements = achievementService.getAllAchievements();
        model.addAttribute("achievements", achievements);

        return "userAchievementForm";
    }

    @PostMapping("/add_edit")
    public String addEditUserAchievement(@Valid @ModelAttribute("userAchievement") UserAchievement userAchievement,
                              BindingResult result,
                              Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "userAchievementForm";
        }

        if (userAchievement.getId() == 0) {
            userAchievementService.createUserAchievement(
                    userAchievement.getUser(),
                    userAchievement.getTask(),
                    userAchievement.getAchievement(),
                    userAchievement.getAchievedDate()
            );
        } else {
            userAchievementService.updateUserAchievement(
                    userAchievement.getId(),
                    userAchievement.getUser(),
                    userAchievement.getTask(),
                    userAchievement.getAchievement(),
                    userAchievement.getAchievedDate()
            );
        }

        return "redirect:/userAchievements";
    }

    @GetMapping("/delete")
    public String deleteUserAchievement(@RequestParam("id") int id,
                                        HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        userAchievementService.deleteUserAchievement(id);
        return "redirect:/userAchievements";
    }
}
