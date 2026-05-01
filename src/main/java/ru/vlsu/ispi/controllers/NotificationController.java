package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vlsu.ispi.beans.FriendRequest;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.UserAchievementService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final UserService userService;

    public NotificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/accept-friend-request/{requestId}")
    public String acceptFriendRequest(@PathVariable int requestId,
                                      HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        userService.acceptFriendRequest(requestId, currentUser.getId());
        return "redirect:/notifications";
    }

    @GetMapping
    public String showNotifications(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<FriendRequest> pendingRequests = userService.getPendingRequests(currentUser.getId());
        List<Notification> notifications = userService.getUnreadNotifications(currentUser.getId());

        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }
}
