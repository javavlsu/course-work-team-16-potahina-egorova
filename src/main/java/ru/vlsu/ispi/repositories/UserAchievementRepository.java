package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.beans.UserAchievement;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {
    List<UserAchievement> findByUser(User user);

    @Query("SELECT ua FROM UserAchievement ua " +
            "LEFT JOIN FETCH ua.achievement " +
            "LEFT JOIN FETCH ua.task " +
            "WHERE ua.user.id = :userId")
    List<UserAchievement> findByUserId(@Param("userId") int userId);

    boolean existsByUserIdAndAchievementId(Integer userId, Integer achievementId);
}
