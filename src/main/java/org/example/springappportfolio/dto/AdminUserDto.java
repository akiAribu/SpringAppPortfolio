package org.example.springappportfolio.dto;

import org.example.springappportfolio.models.UserRole;

import java.time.Instant;

public record AdminUserDto (
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        Instant createdAt
) {}
