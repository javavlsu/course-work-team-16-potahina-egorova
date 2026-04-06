package ru.vlsu.ispi.dao;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.Achievement;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface NotificationDAO {
    void save(Notification notification);
    void update(Notification notification);
    void delete(Notification notification);
    Notification findById(Integer id);
}
