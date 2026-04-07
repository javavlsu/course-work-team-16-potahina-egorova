package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.Notification;
import ru.vlsu.ispi.beans.Task;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
