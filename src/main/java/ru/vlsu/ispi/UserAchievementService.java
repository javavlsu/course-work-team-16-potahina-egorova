package ru.vlsu.ispi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.beans.Task.Category;
import ru.vlsu.ispi.beans.Task.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAchievementService {
    private final UserAchievementRepository userAchievementRepository;

    @Autowired
    public UserAchievementService(UserAchievementRepository userAchievementRepository) {
        this.userAchievementRepository = userAchievementRepository;
    }

    public UserAchievement createUserAchievement(User user, Task task, Achievement achievement,
                                                 LocalDateTime achievedDate) {
        UserAchievement userAchievement = new UserAchievement(user, task,
                achievement, achievedDate);
        return userAchievementRepository.save(userAchievement);
    }

    public Optional<UserAchievement> getUserAchievementById(int id) {
        return userAchievementRepository.findById(id);
    }

    public UserAchievement updateUserAchievement(int id, User user, Task task,
                                                 Achievement achievement,
                                                 LocalDateTime achievedDate) {
        UserAchievement userAchievement = userAchievementRepository.findById(id).orElseThrow(() -> new RuntimeException("UserAchievement not found"));
        userAchievement.setUser(user);
        userAchievement.setTask(task);
        userAchievement.setAchievement(achievement);
        userAchievement.setAchievedDate(achievedDate);

        return userAchievementRepository.save(userAchievement);
    }

    public void deleteUserAchievement(int id) {
        userAchievementRepository.deleteById(id);
    }

    public List<UserAchievement> getAllUserAchievements() {
        return userAchievementRepository.findAll();
    }
}
