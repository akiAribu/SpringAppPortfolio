package org.example.springappportfolio.dto;

import jakarta.validation.constraints.Size;

public record CreateProjectImageRequest(

        @Size(max = 500, message = "Caption must not exceed 500 characters")
        String imageCaption

) {}
