package org.example.springappportfolio.dto;

import org.example.springappportfolio.models.UserRole;

public record ChangeRoleRequest(
        UserRole role
) {}
