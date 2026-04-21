package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.TaskExecutionLog;

import java.util.Optional;

@Repository
public interface TaskExecutionLogRepository extends JpaRepository<TaskExecutionLog, Integer> {
    Optional<TaskExecutionLog> findByTaskId(Integer taskId);
}
