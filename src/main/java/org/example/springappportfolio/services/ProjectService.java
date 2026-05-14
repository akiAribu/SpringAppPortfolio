package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.*;
import org.example.springappportfolio.exceptions.AccessDeniedException;
import org.example.springappportfolio.exceptions.InternalServerErrorException;
import org.example.springappportfolio.exceptions.NotFoundException;
import org.example.springappportfolio.mappers.ProjectMapper;
import org.example.springappportfolio.models.*;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.example.springappportfolio.repositories.ProjectRepository;
import org.example.springappportfolio.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PortfolioRepository portfolioRepository;
    private final ProjectMapper projectMapper;
    private final TagRepository tagRepository;

    public List<ProjectDto> getPortfolioProjects(Long portfolioId, boolean isOwner) {

        //List<Project> projects = projectRepository.findByPortfolioId(portfolioId);
        List<Project> projects = projectRepository.findByPortfolioIdOrderByCreatedAtDesc(portfolioId);

        return projects.stream()
                .filter(project -> isOwner || Boolean.TRUE.equals(project.getIsVisible()))
                .map(projectMapper::toDto)
                .toList();

    }

    public ProjectDto getProjectById(Long projectId, Long portfolioId, boolean isOwner) {

        Project project = getProjectByPortfolio(projectId, portfolioId);

        if (!isOwner && !Boolean.TRUE.equals(project.getIsVisible())) {
            throw new AccessDeniedException("Access denied to hidden project");
        }

        return projectMapper.toDto(project);

    }

    @Transactional
    public ProjectDto createProject(
            Long portfolioId,
            CreateProjectRequest request,
            MultipartFile preview,
            boolean isOwner
    ) {

        validateOwnership(isOwner);

        var portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NotFoundException("Portfolio", portfolioId));

        Project project = new Project();
        project.setPortfolio(portfolio);
        project.setProjectTitle(request.title());
        project.setShortDescription(request.shortDescription());
        project.setFullDescription(request.fullDescription());
        project.setProjectLink(request.projectLink());
        project.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        project.setCreatedAt(Instant.now());

        if (preview != null && !preview.isEmpty()) {
            try {
                project.setProjectPreview(preview.getBytes());
            } catch (java.io.IOException e) {
                throw new InternalServerErrorException("Failed to read preview file", e);
            }
        }

        project = projectRepository.save(project);
        portfolio.getProjects().add(project);

        return projectMapper.toDto(project);

    }

    public ProjectDto createProject(
            Long portfolioId,
            CreateProjectRequest request,
            boolean isOwner
    ) {
        return createProject(portfolioId, request, null, isOwner);
    }

    @Transactional
    public ProjectDto updatePreview(
            Long portfolioId,
            Long projectId,
            MultipartFile file,
            boolean isOwner
    ) {

        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);

        if (file != null && !file.isEmpty()) {
            try {
                project.setProjectPreview(file.getBytes());
            } catch (java.io.IOException e) {
                throw new InternalServerErrorException("Failed to read preview file", e);
            }
        }

        project = projectRepository.save(project);

        return projectMapper.toDto(project);

    }

    @Transactional
    public ProjectDto updateProject(Long portfolioId, Long projectId, UpdateProjectRequest request, boolean isOwner) {

        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);

        if (request.title() != null) {
            project.setProjectTitle(request.title());
        }

        if (request.shortDescription() != null) {
            project.setShortDescription(request.shortDescription());
        }

        if (request.fullDescription() != null) {
            project.setFullDescription(request.fullDescription());
        }

        if (request.projectLink() != null) {
            project.setProjectLink(request.projectLink());
        }

        if (request.isVisible() != null) {
            project.setIsVisible(request.isVisible());
        }

        project = projectRepository.save(project);

        return projectMapper.toDto(project);

    }

    @Transactional
    public void deleteProject(Long portfolioId, Long projectId, boolean isOwner) {

        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);

        projectRepository.delete(project);

    }

    @Transactional
    public ProjectDto updateProjectVisibility(Long portfolioId, Long projectId, boolean isVisible, boolean isOwner) {

        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);
        project.setIsVisible(isVisible);

        project = projectRepository.save(project);

        return projectMapper.toDto(project);

    }

    public List<byte[]> getProjectImages(Long projectId, Long portfolioId, boolean isOwner) {

        Project project = getProjectByPortfolio(projectId, portfolioId);

        if (!isOwner && !Boolean.TRUE.equals(project.getIsVisible())) {
            throw new AccessDeniedException("Access denied to hidden project");
        }

        return project.getProjectImages().stream()
                .map(img -> img.getImageData())
                .toList();

    }

    private Project getProjectByPortfolio(Long projectId, Long  portfolioId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project",  projectId));

        if (!project.getPortfolio().getId().equals(portfolioId)) {
            throw new AccessDeniedException();
        }

        return project;

    }

    private void validateOwnership(boolean isOwner) {
        if (!isOwner) {
            throw new AccessDeniedException();
        }
    }

    @Transactional
    public ProjectDto addImage(
            Long portfolioId,
            Long projectId,
            MultipartFile file,
            CreateProjectImageRequest request,
            boolean isOwner
    ) {

        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);

        ProjectImage image = new ProjectImage();
        image.setProject(project);
        try {
            image.setImageData(file.getBytes());
        } catch (java.io.IOException e) {
            throw new InternalServerErrorException("Failed to read image file", e);
        }
        image.setImageCaption(request != null ? request.imageCaption() : null);

        String contentType = file.getContentType();
        String format = contentType != null ? contentType : "image/jpeg";
        image.setImageFormat(format);

        project.getProjectImages().add(image);
        projectRepository.save(project);

        return projectMapper.toDto(project);

    }

    @Transactional
    public ProjectDto deleteImage(
            Long  portfolioId,
            Long projectId,
            Integer imageId,
            boolean isOwner
    ) {

        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);

        ProjectImage imageToRemove = null;
        for (ProjectImage img : project.getProjectImages()) {
            if (img.getId().equals(imageId)) {
                imageToRemove = img;
                break;
            }
        }

        if (imageToRemove == null) {
            throw new NotFoundException("Image", imageId);
        }

        project.getProjectImages().remove(imageToRemove);
        projectRepository.save(project);

        return projectMapper.toDto(project);

    }

    public List<TagDto> getAvailableTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDto(
                        tag.getId(),
                        tag.getTagName()
                ))
                .toList();
    }

    @Transactional
    public ProjectDto updateProjectTags(
            Long portfolioId,
            Long projectId,
            List<String> tagNames,
            boolean isOwner
    ) {
        validateOwnership(isOwner);

        Project project = getProjectByPortfolio(projectId, portfolioId);

        project.getTags().clear();
        projectRepository.flush();

        for (String tagName : tagNames) {

            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new NotFoundException("Tag", tagName));

            TagInProject relation = new TagInProject();

            relation.setProject(project);
            relation.setTag(tag);

            TagInProjectId id = new TagInProjectId();
            id.setProjectId(project.getId().intValue());
            id.setTagId(tag.getId());
            relation.setId(id);

            project.getTags().add(relation);

        }

        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

}
