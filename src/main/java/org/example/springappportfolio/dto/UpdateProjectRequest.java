package org.example.springappportfolio.dto;

import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Size(max = 500, message = "Short description must not exceed 500 characters")
        String shortDescription,

        @Size(max = 10000, message = "Full description must not exceed 10000 characters")
        String fullDescription,

        String projectLink,
        Boolean isVisible
) {}
