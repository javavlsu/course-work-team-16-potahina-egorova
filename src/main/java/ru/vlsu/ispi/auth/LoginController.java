package ru.vlsu.ispi.auth;

import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<User> userOpt = userService.authenticate(email, password);

        if (userOpt.isPresent()) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Неверные email или пароль");
            return "login";
        }
    }
}
