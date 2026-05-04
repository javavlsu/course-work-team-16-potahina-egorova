package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.beans.UserAchievement;
import ru.vlsu.ispi.repositories.AchievementRepository;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.repositories.UserAchievementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final NotificationService notificationService;

    @Autowired
    public AchievementService(AchievementRepository achievementRepository,
                              UserAchievementRepository userAchievementRepository,
                              NotificationService notificationService) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void checkAndAssignAchievements(User user) {
        int currentPoints = user.getTotalPoints();

        // Получаем все достижения, где reward_points <= totalPoints пользователя
        List<Achievement> availableAchievements = achievementRepository
                .findByRewardPointsLessThanEqualOrderByRewardPointsDesc(currentPoints);

        for (Achievement achievement : availableAchievements) {
            // Проверяем, не получил ли пользователь это достижение ранее
            if (!userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId())) {
                // Создаём запись о получении достижения
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setUser(user);
                userAchievement.setAchievement(achievement);
                userAchievement.setAchievedDate(LocalDateTime.now());
                userAchievementRepository.save(userAchievement);

                // Отправляем уведомление о новом достижении
                notificationService.createAchievementNotification(user, achievement);
            }
        }
    }

    public Achievement createAchievement(String title, String description, int rewardPoints) {
        Achievement achievement = new Achievement(title, description, rewardPoints);
        return achievementRepository.save(achievement);
    }

    public Optional<Achievement> getAchievementById(int id) {
        return achievementRepository.findById(id);
    }

    public Achievement updateAchievement(int id, String title, String description, int rewardPoints) {
        Achievement achievement = achievementRepository.findById(id).orElseThrow(() -> new RuntimeException("Achievement not found"));
        achievement.setTitle(title);
        achievement.setDescription(description);
        achievement.setRewardPoints(rewardPoints);

        return achievementRepository.save(achievement);
    }

    public void deleteAchievement(int id) {
        achievementRepository.deleteById(id);
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
}
