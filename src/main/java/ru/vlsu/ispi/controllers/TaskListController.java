package ru.vlsu.ispi.controllers;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.JPAService;

import javax.servlet.http.HttpSession;
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
    public String allTaskLists(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<TaskList> taskLists = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT tl FROM TaskList tl", TaskList.class).getResultList();
        });

        model.addAttribute("taskLists", taskLists);
        return "taskLists";
    }

    @GetMapping({"/add", "/edit"})
    public String showTaskListForm(@RequestParam(value = "id", required = false) Integer id,
                                   Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (id != null && id > 0) {
            TaskList taskList = jpaService.runInTransaction(entityManager -> {
                TaskList list = entityManager.find(TaskList.class, id);
                if (list != null) {
                    // Принудительно загружаем User
                    Hibernate.initialize(list.getUser());
                }
                return list;
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
        model.addAttribute("users", users);

        return "taskListForm";
    }

    @PostMapping("/add_edit")
    public String addEditTaskList(@Valid @ModelAttribute("taskList") TaskList taskList,
                                  BindingResult result,
                                  Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "taskListForm";
        }

        jpaService.runInTransaction(entityManager -> {
            if (taskList.getId() == 0) {
                entityManager.persist(taskList);
            } else {
                // Обновляем связь с User
                if (taskList.getUser() != null) {
                    taskList.setUser(entityManager.merge(taskList.getUser()));
                }
                entityManager.merge(taskList);
            }
            return null;
        });

        return "redirect:/taskLists";
    }

    @GetMapping("/delete")
    public String deleteTaskList(@RequestParam("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

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
