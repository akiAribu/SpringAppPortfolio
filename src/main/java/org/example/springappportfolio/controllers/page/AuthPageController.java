package org.example.springappportfolio.controllers.page;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.AuthResponse;
import org.example.springappportfolio.dto.LoginRequest;
import org.example.springappportfolio.dto.RegisterRequest;
import org.example.springappportfolio.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

//    @PostMapping("/login")
//    public String login(
//            @Valid @ModelAttribute LoginRequest request,
//            BindingResult bindingResult,
//            Model model) {
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errors", bindingResult.getAllErrors().get(0).getDefaultMessage());
//            return "auth/login";
//        }
//
//        try {
//            AuthResponse response = authService.login(request);
//            model.addAttribute("user", response);
//
//            return "redirect:/profile";
//        } catch (Exception e) {
//            model.addAttribute("errors", e.getMessage());
//            return "auth/login";
//        }
//
//    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("error", errorMessage);
            return "auth/register";
        }

        try {
            authService.register(request);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }

    }

}