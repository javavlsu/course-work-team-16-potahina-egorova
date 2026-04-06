package ru.vlsu.ispi.dao;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TimerMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface TimerModeDAO {
    void save(TimerMode timerMode);
    void update(TimerMode timerMode);
    void delete(TimerMode timerMode);
    TimerMode findById(Integer id);
}
