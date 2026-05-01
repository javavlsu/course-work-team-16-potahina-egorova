package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.User;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserAndIsReadFalse(User user);
    List<Notification> findByUserIdAndIsReadFalse(Integer userId);
}
