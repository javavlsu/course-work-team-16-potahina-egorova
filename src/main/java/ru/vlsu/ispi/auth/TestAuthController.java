package ru.vlsu.ispi.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

@Controller
public class TestAuthController {

    @GetMapping("/profile")
    public String testAuth(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "profile";
        } else {
            return "redirect:/login";
        }
    }
}
