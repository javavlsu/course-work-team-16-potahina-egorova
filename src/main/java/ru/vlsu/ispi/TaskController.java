package ru.vlsu.ispi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final JPAService jpaService;

    public TaskController(JPAService jpaService) {
        this.jpaService = jpaService;
    }

    @GetMapping
    public String allTasks(Model model) {
        List<Task> tasks = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
        });

        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping({"/add", "/edit"})
    public String showTaskForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null && id > 0) {
            Task task = jpaService.runInTransaction(entityManager -> {
                return entityManager.find(Task.class, id);
            });

            if (task == null) {
                return "redirect:/tasks";
            }
            model.addAttribute("task", task);
        } else {
            model.addAttribute("task", new Task());
        }

        List<User> users = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        });
        model.addAttribute("users", users);

        List<TaskList> taskLists = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT tl FROM TaskList tl", TaskList.class).getResultList();
        });
        model.addAttribute("taskLists", taskLists);

        return "taskForm";
    }

    @PostMapping("/add_edit")
    public String addEditTask(@Valid @ModelAttribute("task") Task task,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "taskForm";
        }

        jpaService.runInTransaction(entityManager -> {
            if (task.getId() == 0) { // Создание нового объекта
                entityManager.persist(task);
            } else { // Обновление существующего объекта
                entityManager.merge(task);
            }
            return null;
        });

        return "redirect:/tasks";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam("id") int id) {
        jpaService.runInTransaction(entityManager -> {
            Task task = entityManager.find(Task.class, id);
            if (task != null) {
                entityManager.remove(task);
            }
            return null;
        });
        return "redirect:/tasks";
    }
}
