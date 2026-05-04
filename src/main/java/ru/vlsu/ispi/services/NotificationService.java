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

    public void createTaskCompletionNotification(Task task) {
        User creator = task.getUser();
        User assignedUser = task.getAssignedUser();

        String pointsText = task.getPoints() > 0 ? " и " + task.getPoints() + " XP" : "";

        // Уведомление для создателя задачи
        if (creator != null) {
            Notification creatorNotification = new Notification();
            creatorNotification.setUser(creator);
            creatorNotification.setText(
                    "Задача \"" + task.getTitle() + "\" завершена");
            creatorNotification.setIsRead(false);
            creatorNotification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(creatorNotification);
        }

        // Уведомление для исполнителя
        if (assignedUser != null && !assignedUser.equals(creator)) {
            Notification assignedNotification = new Notification();
            assignedNotification.setUser(assignedUser);
            assignedNotification.setText
                    ("Вы завершили задачу \"" + task.getTitle() + "\"");
            assignedNotification.setIsRead(false);
            assignedNotification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(assignedNotification);
        }
    }

    public List<Notification> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public Optional<Notification> findById(int id) {
        return notificationRepository.findById(id);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }


    public void createAchievementNotification(User user, Achievement achievement) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setText("Поздравляем! Вы получили достижение: " + achievement.getTitle());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
