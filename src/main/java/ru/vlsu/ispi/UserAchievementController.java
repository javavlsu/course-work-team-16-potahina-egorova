package ru.vlsu.ispi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.beans.UserAchievement;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/userAchievements")
public class UserAchievementController {
    private final UserAchievementService userAchievementService;
    private final TaskService taskService;
    private final UserService userService;

    public UserAchievementController(UserAchievementService userAchievementService,
                                     UserService userService, TaskService taskService) {
        this.userAchievementService = userAchievementService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public String allUserAchievements(Model model) {
        List<UserAchievement> userAchievements = userAchievementService.getAllUserAchievements();
        model.addAttribute("userAchievements", userAchievements);
        return "userAchievements";
    }


    @GetMapping({"/add", "/edit"})
    public String showUserAchievementForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
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

        return "userAchievementForm";
    }

    @PostMapping("/add_edit")
    public String addEditUserAchievement(@Valid @ModelAttribute("userAchievement") UserAchievement userAchievement,
                              BindingResult result,
                              Model model) {
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
    public String deleteUserAchievement(@RequestParam("id") int id) {
        userAchievementService.deleteUserAchievement(id);
        return "redirect:/userAchievements";
    }
}
