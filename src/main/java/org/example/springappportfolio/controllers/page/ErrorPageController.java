package org.example.springappportfolio.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorPageController {

    @GetMapping("/error")
    public String handleError(
            @RequestParam(required = false) String message,
            Model model
    ) {
        String errorMessage = message;
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "An error occurred. Please try again.";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "Access denied. You don't have permission to view this page.");
        return "error";
    }

    @GetMapping("/not-found")
    public String notFound(Model model) {
        model.addAttribute("errorMessage", "The page you're looking for was not found.");
        return "error";
    }

}