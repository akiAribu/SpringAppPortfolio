package org.example.springappportfolio.controllers.page;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.AuthResponse;
import org.example.springappportfolio.services.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfilePageController {

    private final AuthService authService;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        AuthResponse currentUser = authService.getCurrentUser(username);

        model.addAttribute("user", currentUser);

        return "profile";
    }
}