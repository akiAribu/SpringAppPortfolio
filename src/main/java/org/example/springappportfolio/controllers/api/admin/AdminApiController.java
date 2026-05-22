package org.example.springappportfolio.controllers.api.admin;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.dto.*;
import org.example.springappportfolio.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;

    @GetMapping("/users/search")
    public ResponseEntity<List<AdminUserDto>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(adminService.searchUsers(q));
    }

    @GetMapping("/users")
    public ResponseEntity<AdminUserPageResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @GetMapping("/users/{id}/projects")
    public ResponseEntity<List<AdminProjectDto>> getUserProjects(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserProjects(id));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> changeUserRole(
            @PathVariable Long id,
            @RequestParam ChangeRoleRequest request,
            Authentication authentication
    ) {
        Long adminId = ((UserPrincipal) authentication.getPrincipal()).getId();
        adminService.changeUserRole(id, request, adminId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long adminId = ((UserPrincipal) authentication.getPrincipal()).getId();
        adminService.deleteUser(id, adminId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<AdminProjectDto> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getProject(id));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        adminService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

}
