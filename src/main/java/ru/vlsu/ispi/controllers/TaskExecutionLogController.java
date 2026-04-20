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

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskExecutionLogController {
    private final TaskExecutionLogService taskExecutionLogService;
    private final MusicMediaService musicMediaService;
    private final VisualMediaService visualMediaService;
    private final TaskService taskService;
    private final UserService userService;


    public TaskExecutionLogController(TaskExecutionLogService taskExecutionLogService,
                                      MusicMediaService musicMediaService,
                                      VisualMediaService visualMediaService,
                                      TaskService taskService,
                                      UserService userService) {
        this.taskExecutionLogService = taskExecutionLogService;
        this.musicMediaService = musicMediaService;
        this.visualMediaService = visualMediaService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/start-execution")
    public String startTaskExecution(
            @RequestParam("taskId") Integer taskId,
            Model model
    ) {
        User currentUser = userService.getTestUser();

        // Получаем задачу, но не передаём её напрямую в newLog
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        TaskExecutionLog newLog = new TaskExecutionLog();
        newLog.setTask(task); // передаём существующую задачу
        newLog.setUser(currentUser);
        newLog.setCompletionReport("");
        newLog.setMusicMedia(null);
        newLog.setVisualMedia(null);

        TaskExecutionLog savedLog = taskExecutionLogService.save(newLog);

        List<MusicMedia> musicMediaList = musicMediaService.getAllMusicMedia();
        List<VisualMedia> visualMediaList = visualMediaService.getAllVisualMedia();

        model.addAttribute("log", savedLog);
        model.addAttribute("musicMediaList", musicMediaList);
        model.addAttribute("visualMediaList", visualMediaList);

        return "taskExecutionLog";
    }

    @GetMapping("/task-execution")
    public String showTaskExecution(@RequestParam("id") Integer logId, Model model) {
        TaskExecutionLog log = taskExecutionLogService.getTaskExecutionLogById(logId)
                .orElseThrow(() -> new RuntimeException("Log not found"));

        model.addAttribute("log", log);

        List<MusicMedia> musicMediaList = musicMediaService.getAllMusicMedia();
        List<VisualMedia> visualMediaList = visualMediaService.getAllVisualMedia();

        model.addAttribute("musicMediaList", musicMediaList);
        model.addAttribute("visualMediaList", visualMediaList);

        return "taskExecutionLog";
    }


    @PostMapping("/task-execution/save-media/{logId}")
    public String saveMediaSelection(
            @PathVariable Integer logId,
            @ModelAttribute TaskExecutionLog updatedLog,
            RedirectAttributes redirectAttributes
    ) {
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
