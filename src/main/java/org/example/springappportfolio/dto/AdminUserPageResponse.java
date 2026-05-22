package org.example.springappportfolio.dto;

import java.util.List;

public record AdminUserPageResponse(
        List<AdminUserDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
