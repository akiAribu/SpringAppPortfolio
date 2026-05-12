package org.example.springappportfolio.dto;

import java.time.Instant;
import java.util.List;

public record ProjectDto(
        Integer projectId,
        Long portfolioId,
        String title,
        String shortDescription,
        String fullDescription,
        String projectLink,
        Boolean isVisible,
        byte[] preview,
        Instant createdAt,
        List<ProjectImageDto> images
) {}
