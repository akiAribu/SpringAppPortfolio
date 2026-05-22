package org.example.springappportfolio.dto;

public record AdminStatsDto(
        long totalUsers,
        long totalProjects,
        long publicPortfolios
) {}
