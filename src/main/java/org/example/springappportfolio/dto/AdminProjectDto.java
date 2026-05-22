package org.example.springappportfolio.dto;

import java.time.Instant;

public record AdminProjectDto(
        Long id,
        String title,
        String shortDescription,
        String ownerUsername,
        Instant createdAt
) {}
