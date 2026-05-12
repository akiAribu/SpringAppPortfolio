package org.example.springappportfolio.dto;

import java.time.Instant;
import java.util.List;

public record PortfolioDto (
        Long portfolioId,
        Long userId,
        String username,
        String email,
        String firstName,
        String lastName,
        byte[] userImage,
        Integer viewsCount,
        Boolean isPublic,
        String bio,
        String specialization,
        Integer experienceYears,
        Instant createdAt,
        Instant updatedAt,
        List<ContactDto> contacts,
        List<ProjectSummary> projects,
        Boolean isOwner
) {}
