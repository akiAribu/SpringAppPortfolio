package org.example.springappportfolio.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/admin_panel";
    }

    @GetMapping("/admin/admin_panel")
    public String adminPanel() {
        return "admin/admin_panel";
    }

    @GetMapping("/admin/users/{id}/projects")
    public String userProjects() {
        return "admin/user_projects";
    }

}
