package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.services.UserAchievementService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserAchievementService userAchievementService;

    public UserController(UserService userService,
                          UserAchievementService userAchievementService) {
        this.userService = userService;
        this.userAchievementService = userAchievementService;
    }

    @GetMapping
    public String listUsers(
            Model model,
            HttpSession session,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setUserId(userId);
        criteria.setName(name);
        criteria.setEmail(email);

        List<User> users = userService.searchUsers(criteria, currentUser.getId());

        // Создаём карту для хранения статуса дружбы
        Map<Integer, Boolean> friendsStatus = new HashMap<>();
        for (User user : users) {
            friendsStatus.put(user.getId(), userService.areFriends(currentUser.getId(), user.getId()));
        }

        model.addAttribute("users", users);
        model.addAttribute("friendsStatus", friendsStatus);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("userId", userId);
        model.addAttribute("name", name);
        model.addAttribute("email", email);

        return "users";
    }

    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable("id") int userId,
                                  Model model,
                                  HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<UserAchievement> achievements = userAchievementService.getAchievementsByUserId(userId);

        model.addAttribute("profileUser", user);
        model.addAttribute("profileUserAchievements", achievements);
        return "userProfile";
    }

    @GetMapping({"/add", "/edit"})
    public String showUserForm(@RequestParam(value = "id", required = false) Integer id,
                               Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (id != null && id > 0) {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }

        return "settings";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "settings";
        }

        int userId = user.getId();
        if (userId == 0) {
            // Создаём нового пользователя
            userService.createUser(
                    user.getName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getTotalPoints()
            );
        } else {
            // Обновляем существующего пользователя
            userService.updateUser(
                    userId,
                    user.getName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getTotalPoints()
            );
        }

        return "redirect:/profile";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult result,
                          Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "settings";
        }

        userService.createUser(
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getTotalPoints()
        );

        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/send-friend-request/{receiverId}")
    public String sendFriendRequest(@PathVariable int receiverId,
                                    HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        userService.sendFriendRequest(currentUser.getId(), receiverId);
        return "redirect:/users";
    }
}
