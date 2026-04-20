package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.TaskExecutionLog;

@Repository
public interface TaskExecutionLogRepository extends JpaRepository<TaskExecutionLog, Integer> {
}
