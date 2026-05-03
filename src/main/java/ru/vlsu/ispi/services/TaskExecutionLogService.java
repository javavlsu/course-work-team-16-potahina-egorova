package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.beans.Task.Category;
import ru.vlsu.ispi.beans.Task.Status;
import ru.vlsu.ispi.repositories.TaskExecutionLogRepository;
import ru.vlsu.ispi.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskExecutionLogService {
    private final TaskExecutionLogRepository taskExecutionLogRepository;

    @Autowired
    public TaskExecutionLogService(TaskExecutionLogRepository taskExecutionLogRepository) {
        this.taskExecutionLogRepository = taskExecutionLogRepository;
    }

    public TaskExecutionLog createTaskExecutionLog(Task task, boolean isReportAttached,
                                                   String completionReport, MusicMedia musicMedia,
                                                   VisualMedia visualMedia, TimerMode timerMode,
                                                   LocalDateTime startTime, LocalDateTime endTime) {
        Optional<TaskExecutionLog> existingActiveLog = findByTaskId(task.getId());
        if (existingActiveLog.isPresent()) {
            throw new RuntimeException("Для этой задачи уже есть активный лог выполнения");
        }

        TaskExecutionLog log = new TaskExecutionLog();
        log.setTask(task);
        log.setIsReportAttached(isReportAttached);
        log.setCompletionReport(completionReport);
        log.setMusicMedia(musicMedia);
        log.setVisualMedia(visualMedia);
        log.setTimerMode(timerMode);
        log.setStartTime(startTime);
        log.setEndTime(endTime);

        return taskExecutionLogRepository.save(log);
    }

    public Optional<TaskExecutionLog> getTaskExecutionLogById(int id) {
        return taskExecutionLogRepository.findById(id);
    }

    public TaskExecutionLog updateTaskExecutionLog(int id, Task task, boolean isReportAttached,
                                                   String completionReport, MusicMedia musicMedia,
                                                   VisualMedia visualMedia, TimerMode timerMode,
                                                   LocalDateTime startTime, LocalDateTime endTime) {
        TaskExecutionLog log = taskExecutionLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task execution log not found"));

        if (task != null) log.setTask(task);
        log.setIsReportAttached(isReportAttached);
        log.setCompletionReport(completionReport);
        if (musicMedia != null) log.setMusicMedia(musicMedia);
        if (visualMedia != null) log.setVisualMedia(visualMedia);
        if (timerMode != null) log.setTimerMode(timerMode);
        if (startTime != null) log.setStartTime(startTime);
        if (endTime != null) log.setEndTime(endTime);

        return taskExecutionLogRepository.save(log);
    }

    public void deleteTaskExecutionLog(int id) {
        taskExecutionLogRepository.deleteById(id);
    }

    public List<TaskExecutionLog> getAllTaskExecutionLogs() {
        return taskExecutionLogRepository.findAll();
    }

    public TaskExecutionLog save(TaskExecutionLog log) {
        return taskExecutionLogRepository.save(log);
    }

    public Optional<TaskExecutionLog> getTaskExecutionLogById(Integer id) {
        return taskExecutionLogRepository.findById(id);
    }

    public Optional<TaskExecutionLog> findByTaskId(Integer taskId) {
        List<TaskExecutionLog> logs = taskExecutionLogRepository.findByTaskId(taskId);

        // Фильтруем только активные логи (где endTime == null)
        return logs.stream()
                .filter(log -> log.getEndTime() == null)
                .findFirst(); // берём первый активный лог
    }
}
