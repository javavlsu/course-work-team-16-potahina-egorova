package ru.vlsu.ispi.controllers;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.vlsu.ispi.beans.*;
import ru.vlsu.ispi.services.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskExecutionLogController {
    private final TaskExecutionLogService taskExecutionLogService;
    private final MusicMediaService musicMediaService;
    private final VisualMediaService visualMediaService;
    private final TaskService taskService;
    private final UserService userService;
    private final NotificationService notificationService;


    public TaskExecutionLogController(TaskExecutionLogService taskExecutionLogService,
                                      MusicMediaService musicMediaService,
                                      VisualMediaService visualMediaService,
                                      TaskService taskService,
                                      UserService userService,
                                      NotificationService notificationService) {
        this.taskExecutionLogService = taskExecutionLogService;
        this.musicMediaService = musicMediaService;
        this.visualMediaService = visualMediaService;
        this.taskService = taskService;
        this.userService = userService;
        this.notificationService = notificationService;
    }


    @GetMapping("/start-execution")
    public String startTaskExecution(
            @RequestParam("taskId") Integer taskId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        if (task.getStatus() == Task.Status.Completed) {
            redirectAttributes.addFlashAttribute("error", "Эта задача уже завершена.");
            return "redirect:/tasks";
        }

        Optional<TaskExecutionLog> existingLog = taskExecutionLogService.findByTaskId(taskId);
        TaskExecutionLog log;

        if (existingLog.isPresent()) {
            log = existingLog.get();
            if (task.getStatus() != Task.Status.InProgress) {
                task.setStatus(Task.Status.InProgress);
                taskService.save(task);
            }
        } else {
            log = new TaskExecutionLog();
            log.setTask(task);
            log.setCompletionReport("");
            log.setStartTime(LocalDateTime.now());
            log = taskExecutionLogService.save(log);
            task.setStatus(Task.Status.Started);
            taskService.save(task);
        }

        model.addAttribute("log", log);
        model.addAttribute("musicMediaList", musicMediaService.getAllMusicMedia());
        model.addAttribute("visualMediaList", visualMediaService.getAllVisualMedia());
        return "taskExecutionLog";
    }

    @GetMapping("/task-execution")
    public String showTaskExecution(@RequestParam("id") Integer logId, Model model,
                                    HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        TaskExecutionLog log = taskExecutionLogService.getTaskExecutionLogById(logId)
                .orElseThrow(() -> new RuntimeException("Log not found"));

        model.addAttribute("log", log);

        List<MusicMedia> musicMediaList = musicMediaService.getAllMusicMedia();
        List<VisualMedia> visualMediaList = visualMediaService.getAllVisualMedia();

        model.addAttribute("musicMediaList", musicMediaList);
        model.addAttribute("visualMediaList", visualMediaList);

        return "taskExecutionLog";
    }

    @GetMapping("/complete-execution")
    public String showCompletionForm(@RequestParam("logId") Integer logId,
                                     Model model) {
        TaskExecutionLog log =
                taskExecutionLogService.getTaskExecutionLogById(logId)
                        .orElseThrow(() -> new RuntimeException("Лог не найден"));
        model.addAttribute("log", log);
        return "taskCompletion"; // Новая страница
    }


    @PostMapping("/finish-execution")
    public String finishTaskExecution(@RequestParam("logId") Integer logId,
                                      @RequestParam("report") String report) {
        TaskExecutionLog log =
                taskExecutionLogService.getTaskExecutionLogById(logId)
                        .orElseThrow(() -> new RuntimeException("Лог не найден"));

        log.setCompletionReport(report);
        log.setIsReportAttached(report != null && !report.trim().isEmpty());
        log.setEndTime(LocalDateTime.now());
        taskExecutionLogService.save(log);

        Task task = log.getTask();
        task.setStatus(Task.Status.Completed);
        taskService.save(task);

        User assignedUser = task.getAssignedUser();
        if (assignedUser != null) {
            assignedUser.setTotalPoints(assignedUser.getTotalPoints() + task.getPoints());
            userService.save(assignedUser);
        }

        // Отправляем уведомления
        notificationService.createTaskCompletionNotification(task);

        return "redirect:/tasks";
    }

    @PostMapping("/task-execution/save-media/{logId}")
    public String saveMediaSelection(@PathVariable Integer logId,
            @ModelAttribute TaskExecutionLog updatedLog,
            RedirectAttributes redirectAttributes,
                                     HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        TaskExecutionLog existingLog = taskExecutionLogService
                .getTaskExecutionLogById(logId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Запись выполнения задачи не найдена"
                ));

        existingLog.setMusicMedia(updatedLog.getMusicMedia());
        existingLog.setVisualMedia(updatedLog.getVisualMedia());

        taskExecutionLogService.save(existingLog);

        redirectAttributes.addFlashAttribute("success", "Медиа успешно сохранены");
        return "redirect:/tasks/task-execution?id=" + logId;
    }
}
