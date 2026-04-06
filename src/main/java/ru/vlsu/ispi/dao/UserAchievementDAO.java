package ru.vlsu.ispi.dao;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.UserAchievement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface UserAchievementDAO {
    void save(UserAchievement userAchievement);
    void update(UserAchievement userAchievement);
    void delete(UserAchievement userAchievement);
    UserAchievement findById(Integer id);
}
