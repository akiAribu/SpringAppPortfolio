package org.example.springappportfolio.dto;

import java.time.Instant;

public record ProjectImageDto(
        Integer imageId,
        byte[] imageData,
        String imageFormat,
        String imageCaption,
        Instant createdAt
) {}
