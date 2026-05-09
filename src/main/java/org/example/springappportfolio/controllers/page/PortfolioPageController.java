package org.example.springappportfolio.controllers.page;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.dto.PortfolioDto;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.services.PortfolioService;
import org.example.springappportfolio.services.SecurityService;
import org.example.springappportfolio.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PortfolioPageController {

    private final UserService userService;
    private final PortfolioService portfolioService;
    private final SecurityService securityService;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        PortfolioDto portfolio = portfolioService.getMyPortfolio(authentication);

        model.addAttribute("portfolio", portfolio);
        model.addAttribute("isOwner", true);

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Authentication authentication, Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Long userId = ((UserPrincipal) authentication.getPrincipal()).getId();

        User user = userService.findById(userId);
        model.addAttribute("user", user);

        return "edit_profile";
    }

   @GetMapping("/u/{userId}")
   public String viewUserPortfolio(
           @PathVariable Long userId,
           Authentication authentication,
           Model model
   ) {
        try {
            PortfolioDto portfolio = portfolioService.getPortfolioByUserId(userId, authentication);

            if (Boolean.TRUE.equals(portfolio.isOwner())) {
                return "redirect:/profile";
            }

            model.addAttribute("portfolio", portfolio);
            model.addAttribute("isOwner", portfolio.isOwner());

            return "portfolio_view";

        } catch (SecurityException e) {

            model.addAttribute("error", "This portfolio is private");
            return "error";

        } catch (Exception e) {

            model.addAttribute("error", "User not found");
            return "error";

        }
   }
}