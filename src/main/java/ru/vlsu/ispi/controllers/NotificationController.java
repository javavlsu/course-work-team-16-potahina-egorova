package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vlsu.ispi.beans.User;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    @GetMapping
    public String showNotifications(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        return "notifications";
    }
}
