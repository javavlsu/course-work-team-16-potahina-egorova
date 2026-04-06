package ru.vlsu.ispi.dao;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface AchievementDAO {
    void save(Achievement achievement);
    void update(Achievement achievement);
    void delete(Achievement achievement);
    Achievement findById(Integer id);
}
