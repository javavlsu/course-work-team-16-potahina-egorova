package ru.vlsu.ispi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.User;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping({"/add", "/edit"})
    public String showUserForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
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
                           Model model) {
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

        return "redirect:/users";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult result,
                          Model model) {
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
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/table")
    public String showTable(@RequestParam("id") int id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "table";
    }

    @GetMapping("/user_table")
    public String toUsersTable(@RequestParam("id") int id) {
        return "redirect:/users/table?id=" + id;
    }
}
