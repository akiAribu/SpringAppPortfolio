package org.example.springappportfolio.dto;

import org.example.springappportfolio.models.UserRole;

import java.time.Instant;

public record AuthResponse (
        Long userId,
        String username,
        String email,
        UserRole role,
        Instant createdAt,
        String message
) {}
