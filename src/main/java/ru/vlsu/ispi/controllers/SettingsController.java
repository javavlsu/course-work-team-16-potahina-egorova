package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vlsu.ispi.beans.User;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @GetMapping
    public String showSettingsForm(@RequestParam(value = "id", required = false) Integer id,
                                   Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "settings";
    }
}
