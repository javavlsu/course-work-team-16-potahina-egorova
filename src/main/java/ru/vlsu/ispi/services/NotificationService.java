package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.beans.Task.Category;
import ru.vlsu.ispi.beans.Task.Status;
import ru.vlsu.ispi.repositories.NotificationRepository;
import ru.vlsu.ispi.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(User user, String text) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setText(text);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserAndIsReadFalse(user);
    }

    public Optional<Notification> getNotificationById(int id) {
        return notificationRepository.findById(id);
    }

    public Notification updateNotification(int id, User user, Task task, UserAchievement userAchievement,
                                           String title, String text, boolean isRead) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setUser(user);
        notification.setTask(task);
        notification.setUserAchievement(userAchievement);
        notification.setTitle(title);
        notification.setText(text);
        notification.setIsRead(isRead);

        return notificationRepository.save(notification);
    }

    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
