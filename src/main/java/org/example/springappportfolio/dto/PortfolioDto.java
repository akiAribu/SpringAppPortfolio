package org.example.springappportfolio.dto;

import java.time.Instant;
import java.util.List;

public record PortfolioDto (
        Long portfolioId,
        Long userId,
        String username,
        String email,
        byte[] userImage,
        Integer viewsCount,
        Boolean isPublic,
        Instant createdAt,
        Instant updatedAt,
        List<ContactDto> contacts,
        //List<ProjectSummary> projects
        Boolean isOwner
) {}
