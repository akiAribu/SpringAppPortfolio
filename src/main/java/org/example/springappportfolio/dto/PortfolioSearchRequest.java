package org.example.springappportfolio.dto;

import org.example.springappportfolio.models.Specialization;

import java.util.List;

public record PortfolioSearchRequest(
   Specialization specialization,
   Integer minExperience,
   Integer maxExperience,
   List<String> tags,
   List<Specialization> specializations,
   List<String> experienceRanges,
   Integer page,
   Integer size
) {
    public PortfolioSearchRequest {
        if (page == null) page = 0;
        if (size == null) size = 8;
    }
}
