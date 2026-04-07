package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.repositories.AchievementRepository;
import ru.vlsu.ispi.beans.Achievement;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;

    @Autowired
    public AchievementService(AchievementRepository userAchievementRepository) {
        this.achievementRepository = userAchievementRepository;
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
