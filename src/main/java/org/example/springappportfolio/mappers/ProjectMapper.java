package org.example.springappportfolio.mappers;

import org.example.springappportfolio.dto.ProjectDto;
import org.example.springappportfolio.dto.ProjectImageDto;
import org.example.springappportfolio.dto.TagDto;
import org.example.springappportfolio.models.Project;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {

    private final ProjectImageMapper projectImageMapper;

    public ProjectMapper(ProjectImageMapper projectImageMapper) {
        this.projectImageMapper = projectImageMapper;
    }

    public ProjectDto toDto(Project project) {

        if  (project == null) {
            return null;
        }

        List<ProjectImageDto> images = project.getProjectImages().stream()
                .map(projectImageMapper::toDto)
                .toList();

        List<TagDto> tags = project.getTags().stream()
                .map(tagInProject -> new TagDto(
                        tagInProject.getTag().getId(),
                        tagInProject.getTag().getTagName()
                ))
                .toList();

        return new ProjectDto(
                project.getId(),
                project.getPortfolio().getId(),
                project.getProjectTitle(),
                project.getShortDescription(),
                project.getFullDescription(),
                project.getProjectLink(),
                project.getIsVisible(),
                project.getProjectPreview(),
                project.getCreatedAt(),
                images,
                tags
        );

    }

    public ProjectDto toDto(Project project, Long portfolioId) {

        if  (project == null) {
            return null;
        }

        List<ProjectImageDto> images = project.getProjectImages().stream()
                .map(projectImageMapper::toDto)
                .toList();

        List<TagDto> tags = project.getTags().stream()
                .map(tagInProject -> new TagDto(
                        tagInProject.getTag().getId(),
                        tagInProject.getTag().getTagName()
                ))
                .toList();

        return new ProjectDto(
                project.getId(),
                portfolioId,
                project.getProjectTitle(),
                project.getShortDescription(),
                project.getFullDescription(),
                project.getProjectLink(),
                project.getIsVisible(),
                project.getProjectPreview(),
                project.getCreatedAt(),
                images,
                tags
        );

    }

}
