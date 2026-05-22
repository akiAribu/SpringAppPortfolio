package org.example.springappportfolio.controllers.page;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.dto.PortfolioDto;
import org.example.springappportfolio.dto.ProjectDto;
import org.example.springappportfolio.dto.TagDto;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.services.PortfolioService;
import org.example.springappportfolio.services.ProjectService;
import org.example.springappportfolio.services.SecurityService;
import org.example.springappportfolio.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PortfolioPageController {

    private final UserService userService;
    private final PortfolioService portfolioService;
    private final SecurityService securityService;
    private final ProjectService projectService;

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
        PortfolioDto portfolio = portfolioService.getMyPortfolio(authentication);

        model.addAttribute("user", user);
        model.addAttribute("portfolio", portfolio);

        return "edit_profile";
    }


    @GetMapping("/u/{userId}")
    public String viewUserPortfolio(
            @PathVariable Long userId,
            Authentication authentication,
            Model model
    ) {
        PortfolioDto portfolio = portfolioService.getPortfolioByUserId(userId, authentication);

        if (Boolean.TRUE.equals(portfolio.isOwner())) {
            return "redirect:/profile";
        }

        model.addAttribute("portfolio", portfolio);
        model.addAttribute("isOwner", portfolio.isOwner());

        return "portfolio_view";
    }

    @GetMapping("/project/{projectId}")
    public String viewProject(
            @PathVariable Long projectId,
            @RequestParam Long portfolioId,
            Authentication authentication,
            Model model
    ) {
        boolean isOwner = securityService.isOwner(portfolioId, authentication) || securityService.isAdmin(authentication);

        ProjectDto project = projectService.getProjectById(projectId, portfolioId, isOwner);
        List<byte[]> images = projectService.getProjectImages(projectId, portfolioId, isOwner);

        PortfolioDto portfolio = portfolioService.getPortfolioById(portfolioId, authentication);

        model.addAttribute("project", project);
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("userId", portfolio.userId());
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("images", images);

        List<String> projectTagNames = project.tags() != null
                ? project.tags().stream().map(TagDto::name).toList()
                : List.of();
        model.addAttribute("projectTagNames", projectTagNames);

        return "project_detail";
    }

}