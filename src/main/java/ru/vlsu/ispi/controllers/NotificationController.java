package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.FriendRequest;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.NotificationService;
import ru.vlsu.ispi.services.UserAchievementService;
import ru.vlsu.ispi.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;

    public NotificationController(UserService userService,
                                  NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String showNotifications(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<FriendRequest> pendingRequests = userService.getPendingRequests(currentUser.getId());
        List<Notification> notifications = notificationService.getUnreadNotifications(currentUser.getId());

        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("notifications", notifications);
        return "notifications";
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

    @PostMapping("/reject-friend-request/{requestId}")
    public String rejectFriendRequest(@PathVariable int requestId,
                                      HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        userService.rejectFriendRequest(requestId, currentUser.getId());
        return "redirect:/notifications";
    }

    @PostMapping("/mark-read/{notificationId}")
    @ResponseBody
    public Map<String, Object> markNotificationAsRead(@PathVariable Integer notificationId,
                                                      HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Пользователь не авторизован");
            return response;
        }

        Optional<Notification> notificationOpt = notificationService.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            if (notification.getUser().getId() == currentUser.getId()) {
                notification.setIsRead(true);
                notificationService.save(notification);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
}
