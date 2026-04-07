package ru.vlsu.ispi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.UserAchievement;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {
}
