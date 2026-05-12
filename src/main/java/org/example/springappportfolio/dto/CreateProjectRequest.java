package org.example.springappportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(

        @NotBlank(message = "Title is required")
        String title,

        @Size(max = 500, message = "Short description must not exceed 500 characters")
        String shortDescription,

        @Size(max = 10000, message = "Full description must not exceed 10000 characters")
        String fullDescription,

        String projectLink,
        Boolean isVisible

) {}
