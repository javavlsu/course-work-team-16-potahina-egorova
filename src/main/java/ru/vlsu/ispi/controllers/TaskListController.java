package ru.vlsu.ispi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.JPAService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/taskLists")
public class TaskListController {
    private final JPAService jpaService;

    public TaskListController(JPAService jpaService) {
        this.jpaService = jpaService;
    }

    @GetMapping
    public String allTaskLists(Model model) {
        List<TaskList> taskLists = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT tl FROM TaskList tl", TaskList.class).getResultList();
        });

        model.addAttribute("taskLists", taskLists);
        return "taskLists";
    }

    @GetMapping({"/add", "/edit"})
    public String showTaskListForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null && id > 0) {
            TaskList taskList = jpaService.runInTransaction(entityManager -> {
                return entityManager.find(TaskList.class, id);
            });

            if (taskList == null) {
                return "redirect:/taskLists";
            }
            model.addAttribute("taskList", taskList);
        } else {
            model.addAttribute("taskList", new TaskList());
        }

        List<User> users = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        });
        model.addAttribute("users", users); // Передайте список категорий в шаблон

        return "taskListForm";
    }

    @PostMapping("/add_edit")
    public String addEditTaskList(@Valid @ModelAttribute("taskList") TaskList taskList,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "taskListForm";
        }

        jpaService.runInTransaction(entityManager -> {
            if (taskList.getId() == 0) { // Создание нового объекта
                entityManager.persist(taskList);
            } else { // Обновление существующего объекта
                entityManager.merge(taskList);
            }
            return null;
        });

        return "redirect:/taskLists";
    }

    @GetMapping("/delete")
    public String deleteTaskList(@RequestParam("id") int id) {
        jpaService.runInTransaction(entityManager -> {
            TaskList taskList = entityManager.find(TaskList.class, id);
            if (taskList != null) {
                entityManager.remove(taskList);
            }
            return null;
        });
        return "redirect:/taskLists";
    }
}
