package ru.vlsu.ispi.dao;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.TaskExecutionLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface TaskExecutionLogDAO {
    void save(TaskExecutionLog taskExecutionLog);
    void update(TaskExecutionLog taskExecutionLog);
    void delete(TaskExecutionLog taskExecutionLog);
    TaskExecutionLog findById(Integer id);
}
