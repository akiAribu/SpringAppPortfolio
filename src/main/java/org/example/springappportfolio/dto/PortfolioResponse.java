package org.example.springappportfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PortfolioResponse {
    private Long userId;
    private String username;
    private String email;

    private Long portfolioId;
    private Integer viewsCount;
    private Integer portfolioCreatedAt;
    private Integer portfolioUpdatedAt;

    private List<ProjectSummary> projects;
}
