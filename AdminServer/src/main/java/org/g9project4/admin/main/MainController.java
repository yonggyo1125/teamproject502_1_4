package org.g9project4.admin.main;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String index(HttpSession session) {
        String test = (String)session.getAttribute("test");
        System.out.println(test);
        return "main/index";
    }
}
