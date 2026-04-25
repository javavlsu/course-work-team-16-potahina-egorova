package ru.vlsu.ispi.controllers;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.services.JPAService;

import javax.persistence.TypedQuery;
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

//    @GetMapping
//    public String showUserTaskLists(Model model, HttpSession session) {
//        User currentUser = (User) session.getAttribute("user");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//
//        List<TaskList> taskLists = jpaService.runInTransaction(entityManager -> {
//            TypedQuery<TaskList> query = entityManager.createQuery(
//                    ("SELECT tl FROM TaskList tl WHERE tl.user = :user"), TaskList.class);
//            query.setParameter("user", currentUser);
//            return query.getResultList();
//        });
//
//        model.addAttribute("taskLists", taskLists);
//        return "taskLists";
//    }

    @GetMapping
    public String showUserTaskLists(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search,
            Model model, HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<TaskList> taskListPage = jpaService.runInTransaction(entityManager -> {
            String queryString = "SELECT tl FROM TaskList tl WHERE tl.user = :user";
            if (search != null && !search.trim().isEmpty()) {
                queryString += " AND LOWER(tl.title) LIKE LOWER(CONCAT('%', :search, '%'))";
            }
            queryString += " ORDER BY tl.id DESC";

            TypedQuery<TaskList> query = entityManager.createQuery(queryString, TaskList.class);
            query.setParameter("user", currentUser);
            if (search != null && !search.trim().isEmpty()) {
                query.setParameter("search", search);
            }

            // Подсчёт общего количества элементов
            String countQueryString = "SELECT COUNT(tl) FROM TaskList tl WHERE tl.user = :user";
            if (search != null && !search.trim().isEmpty()) {
                countQueryString += " AND LOWER(tl.title) LIKE LOWER(CONCAT('%', :search, '%'))";
            }
            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString, Long.class);
            countQuery.setParameter("user", currentUser);
            if (search != null && !search.trim().isEmpty()) {
                countQuery.setParameter("search", search);
            }

            long total = countQuery.getSingleResult();
            int start = page * size;
            query.setFirstResult(start);
            query.setMaxResults(size);

            List<TaskList> content = query.getResultList();
            return new PageImpl<>(content, pageable, total);
        });

        model.addAttribute("taskLists", taskListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskListPage.getTotalPages());
        model.addAttribute("totalElements", taskListPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);

        return "taskLists";
    }

    @GetMapping("/tasksInTaskList")
    public String showTasksInList(
            @RequestParam("id") Integer listId,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        TaskList taskList = jpaService.runInTransaction(entityManager -> {
            TaskList list = entityManager.find(TaskList.class, listId);
            if (list != null) {
                // Принудительно загружаем задачи и пользователя
                Hibernate.initialize(list.getTasks());
                Hibernate.initialize(list.getUser());
            }
            return list;
        });

        // Проверяем, что список найден и принадлежит текущему пользователю
        if (taskList == null || taskList.getUser().getId() != currentUser.getId()) {
            // Если список не найден или не принадлежит пользователю, перенаправляем на страницу со списками
            return "redirect:/taskLists";
        }

        // Добавляем данные в модель для отображения в шаблоне
        model.addAttribute("taskList", taskList);

        return "tasksInTaskList";
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
