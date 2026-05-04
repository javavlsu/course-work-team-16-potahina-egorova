package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.Achievement;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    List<Achievement> findByRewardPointsLessThanEqualOrderByRewardPointsDesc(int points);
}
