package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.*;
import org.example.springappportfolio.exceptions.AccessDeniedException;
import org.example.springappportfolio.exceptions.NotFoundException;
import org.example.springappportfolio.models.Project;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.example.springappportfolio.repositories.ProjectRepository;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public AdminUserPageResponse getAllUsers(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<User> userPage = userRepository.findAll(pageRequest);
        List<AdminUserDto> content = userPage.getContent().stream()
                .map(this::toAdminUserDto)
                .toList();
        return new AdminUserPageResponse(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.hasNext(),
                userPage.hasPrevious()
        );
    }

    public AdminUserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        return toAdminUserDto(user);
    }

    @Transactional
    public void changeUserRole(Long userId, ChangeRoleRequest request, Long adminId) {
        if (userId.equals(adminId)) {
            throw new AccessDeniedException("Cannot change your own role");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        user.setUserRole(request.role());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId, Long adminId) {
        if (userId.equals(adminId)) {
            throw new AccessDeniedException("Cannot delete yourself");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        userRepository.delete(user);
    }

    public AdminProjectDto getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project", projectId));
        return toAdminProjectDto(project);
    }

    public List<AdminProjectDto> getUserProjects(Long  userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        if (user.getPortfolio() == null) {
            return List.of();
        }

        return projectRepository.findByPortfolioIdOrderByCreatedAtDesc(user.getPortfolio().getId())
                .stream()
                .map(this::toAdminProjectDto)
                .toList();
    }

    public List<AdminUserDto> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query).stream()
                .map(this::toAdminUserDto)
                .toList();
    }

    @Transactional
    public void deleteProject(Long projectId) {
        projectService.deleteAsAdmin(projectId);
    }

    public AdminStatsDto getStats() {
        long totalUsers = userRepository.count();
        long totalProjects = projectRepository.count();
        long publicPortfolios = portfolioRepository.countPublicPortfolios();

        return new AdminStatsDto(totalUsers, totalProjects, publicPortfolios);
    }

    private AdminUserDto toAdminUserDto(User user) {
        return new AdminUserDto(
          user.getId(),
          user.getUsername(),
          user.getEmail(),
          user.getFirstName(),
          user.getLastName(),
          user.getUserRole(),
          user.getCreatedAt()
        );
    }

    private AdminProjectDto toAdminProjectDto(Project project) {
        return new AdminProjectDto(
                project.getId(),
                project.getProjectTitle(),
                project.getShortDescription(),
                project.getPortfolio().getUser().getUsername(),
                project.getCreatedAt()
        );
    }

}
