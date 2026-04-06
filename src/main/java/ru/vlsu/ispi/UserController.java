package ru.vlsu.ispi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.ispi.beans.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    private final JPAService jpaService;

    public UserController(JPAService jpaService) {
        this.jpaService = jpaService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = jpaService.runInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        });

        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping({"/add", "/edit"})
    public String showUserForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null && id > 0) {
            User user = jpaService.runInTransaction(entityManager -> {
                return entityManager.find(User.class, id);
            });

            if (user == null) {
                return "redirect:/users";
            }
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }

        return "settings";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute("user") User user,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "settings";
        }

        jpaService.runInTransaction(entityManager -> {
            if (user.getId() == 0) {
                entityManager.persist(user);
            } else {
                entityManager.merge(user);
            }
            return null;
        });

        return "redirect:/users";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "settings";
        }

        jpaService.runInTransaction(entityManager -> {
            if (user.getId() == 0) {
                entityManager.persist(user);
            } else {
                entityManager.merge(user);
            }
            return null;
        });

        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        jpaService.runInTransaction(entityManager -> {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
            }
            return null;
        });
        return "redirect:/users";
    }

    @GetMapping("/table")
    public String showTable(@RequestParam("id") int id, Model model) {
        // Здесь можно получить пользователя по id и добавить в модель, если нужно
        User user = jpaService.runInTransaction(em -> em.find(User.class, id));
        model.addAttribute("user", user);
        return "table"; // имя вашей страницы для отображения таблицы
    }

    @GetMapping("/user_table")
    public String toUsersTable(@RequestParam("id") int id) {
        return "redirect:/users/table?id=" + id;
    }
}
