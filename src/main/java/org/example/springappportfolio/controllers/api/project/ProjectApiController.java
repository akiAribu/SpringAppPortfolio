package org.example.springappportfolio.controllers.api.project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.*;
import org.example.springappportfolio.exceptions.AccessDeniedException;
import org.example.springappportfolio.exceptions.ValidationException;
import org.example.springappportfolio.services.PortfolioService;
import org.example.springappportfolio.services.ProjectService;
import org.example.springappportfolio.services.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectApiController {

    private final ProjectService projectService;
    private final PortfolioService portfolioService;
    private  final SecurityService securityService;

    @GetMapping("/portfolios/{portfolioId}/projects")
    public ResponseEntity<List<ProjectDto>> getPortfolioProjects(
            @PathVariable Long portfolioId,
            Authentication authentication
    ) {

        boolean isOwner = securityService.isOwner(portfolioId, authentication) || securityService.isAdmin(authentication);

        return ResponseEntity.ok(
                projectService.getPortfolioProjects(portfolioId, isOwner)
        );

    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDto> getProject(
            @PathVariable Long projectId,
            @RequestParam Long portfolioId,
            Authentication authentication
    ) {

        boolean isOwner = securityService.isOwner(portfolioId, authentication) || securityService.isAdmin(authentication);

        return ResponseEntity.ok(
                projectService.getProjectById(projectId, portfolioId, isOwner)
        );

    }

    @PostMapping("/portfolios/me/projects")
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody @Valid CreateProjectRequest request,
            Authentication authentication
    ) {

        Long portfolioId = portfolioService.getPortfolioId(authentication);

        return ResponseEntity.status(201).body(
                projectService.createProject(portfolioId, request, true)
        );

    }

    @PostMapping("/portfolios/me/projects/with-preview")
    public ResponseEntity<ProjectDto> createProjectWithPreview(
            @RequestParam("title") String title,
            @RequestParam(value = "shortDescription", required = false) String shortDescription,
            @RequestParam(value = "fullDescription", required = false) String fullDescription,
            @RequestParam(value = "projectLink", required = false) String projectLink,
            @RequestParam(value = "isVisible", required = false, defaultValue = "true") Boolean isVisible,
            @RequestParam(value = "preview", required = false) MultipartFile preview,
            Authentication authentication
    ) {

        ValidationException validationEx = new ValidationException("Validation failed");

        if (title == null || title.isBlank()) {
            validationEx.addError("title", "Title is required");
        }
        if (title != null && title.length() > 200) {
            validationEx.addError("title", "Title must not exceed 200 characters");
        }
        if (shortDescription != null && shortDescription.length() > 500) {
            validationEx.addError("shortDescription", "Short description must not exceed 500 characters");
        }
        if (fullDescription != null && fullDescription.length() > 10000) {
            validationEx.addError("fullDescription", "Full description must not exceed 10000 characters");
        }

        if (!validationEx.getErrors().isEmpty()) {
            throw validationEx;
        }

        Long portfolioId = portfolioService.getPortfolioId(authentication);

        CreateProjectRequest request = new CreateProjectRequest(
                title, shortDescription, fullDescription, projectLink, isVisible
        );

        return ResponseEntity.status(201).body(
                projectService.createProject(portfolioId, request, preview, true)
        );

    }

    @PostMapping("/projects/{projectId}/preview")
    public ResponseEntity<ProjectDto> uploadPreview(
            @PathVariable Long projectId,
            @RequestParam Long portfolioId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        Long myPortfolioId = portfolioService.getPortfolioId(authentication);
        if (!myPortfolioId.equals(portfolioId)) {
            throw new AccessDeniedException();
        }

        return ResponseEntity.ok(
                projectService.updatePreview(portfolioId, projectId, file, true)
        );

    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long projectId,
            @RequestParam Long portfolioId,
            @RequestBody @Valid UpdateProjectRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                projectService.updateProject(portfolioId, projectId, request, true)
        );

    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId,
            @RequestParam Long portfolioId,
            Authentication authentication
    ) {

        projectService.deleteProject(portfolioId, projectId, true);

        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/projects/{projectId}/visibility")
    public ResponseEntity<ProjectDto> updateProjectVisibility(
            @PathVariable Long projectId,
            @RequestParam Long portfolioId,
            @RequestParam Boolean isVisible,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                projectService.updateProjectVisibility(portfolioId, projectId, isVisible, true)
        );

    }

    @GetMapping("/projects/{projectId}/preview")
    public ResponseEntity<byte[]> getProjectPreview(
        @PathVariable Long projectId,
        @RequestParam Long portfolioId,
        Authentication authentication
    ) {
        ProjectDto project = projectService.getProjectById(projectId, portfolioId,
                securityService.isOwner(portfolioId, authentication));

        byte[] preview = project.preview();
        if (preview == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(preview);
    }

    @GetMapping("/projects/{projectId}/image/{imageId}")
    public ResponseEntity<byte[]> getProjectImage(
            @PathVariable Long projectId,
            @PathVariable Integer imageId,
            @RequestParam Long portfolioId,
            Authentication authentication
    ) {

        boolean isOwner = securityService.isOwner(portfolioId, authentication);

        ProjectDto project = projectService.getProjectById(projectId, portfolioId, isOwner);

        if (project.images() == null) {
            return ResponseEntity.notFound().build();
        }

        ProjectImageDto imageDto = project.images().stream()
                .filter(img -> img.imageId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (imageDto == null || imageDto.imageData() == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = imageDto.imageFormat() != null ? imageDto.imageFormat() : "image/jpeg";

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(imageDto.imageData());

    }

    @PostMapping("/projects/{projectId}/images")
    public ResponseEntity<ProjectDto> addImage(
        @PathVariable Long projectId,
        @RequestParam Long portfolioId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "caption", required = false) String caption,
        Authentication authentication
    ) {

        Long myPortfolioId = portfolioService.getPortfolioId(authentication);
        if (!myPortfolioId.equals(portfolioId)) {
            throw new AccessDeniedException();
        }

        CreateProjectImageRequest request = new CreateProjectImageRequest(caption);

        return ResponseEntity.ok(
                projectService.addImage(portfolioId, projectId, file, request, true)
        );

    }

    @DeleteMapping("/projects/{projectId}/images/{imageId}")
    public ResponseEntity<ProjectDto> deleteImage(
            @PathVariable Long projectId,
            @PathVariable Integer imageId,
            @RequestParam Long portfolioId,
            Authentication authentication
    ) {

        Long myPortfolioId = portfolioService.getPortfolioId(authentication);
        if (!myPortfolioId.equals(portfolioId)) {
            throw new AccessDeniedException();
        }

        return ResponseEntity.ok(
                projectService.deleteImage(portfolioId, projectId, imageId, true)
        );

    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> getAllTags() {
        return ResponseEntity.ok(
                projectService.getAvailableTags()
        );
    }

    @PutMapping("/projects/{projectId}/tags")
    public ResponseEntity<ProjectDto> updateProjectTags(
        @PathVariable Long projectId,
        @RequestParam Long portfolioId,
        @RequestBody UpdateProjectTagsRequest request,
        Authentication authentication
    ) {
        Long myPortfolioId = portfolioService.getPortfolioId(authentication);

        if (!myPortfolioId.equals(portfolioId)) {
            throw new AccessDeniedException();
        }

        return ResponseEntity.ok(
                projectService.updateProjectTags(
                        portfolioId,
                        projectId,
                        request.tags(),
                        true
                )
        );
    }

}
