package org.example.springappportfolio.dto;

import jakarta.validation.constraints.Size;

import java.time.Instant;

public record ProjectImageDto(
        Integer imageId,
        byte[] imageData,
        String imageFormat,
        @Size(max = 500, message = "Caption must not exceed 500 characters")
        String imageCaption,
        Instant createdAt
) {}
