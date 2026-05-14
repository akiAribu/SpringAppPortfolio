package org.example.springappportfolio.dto;

import java.util.List;

public record PortfolioPageResponse(
        List<PortfolioCardDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}
