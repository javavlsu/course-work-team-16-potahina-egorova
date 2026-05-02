package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.beans.UserAchievement;
import ru.vlsu.ispi.services.UserAchievementService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserAchievementService userAchievementService;
    private final UserService userService;

    public ProfileController(UserAchievementService userAchievementService,
                             UserService userService) {
        this.userAchievementService = userAchievementService;
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Загружаем обе коллекции в рамках транзакции
        List<User> allFriends = currentUser.getAllFriends();

        List<UserAchievement> userAchievements = userAchievementService.getUserAchievementsByUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("friends", allFriends); // передаём объединённый список
        model.addAttribute("userAchievements", userAchievements);
        return "profile";
    }
}
