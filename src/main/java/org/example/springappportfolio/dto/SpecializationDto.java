package org.example.springappportfolio.dto;

import org.example.springappportfolio.models.Specialization;

public record SpecializationDto (
        String code,
        String displayName
) {
    public static SpecializationDto from(Specialization specialization) {
        return new SpecializationDto(
                specialization.name(),
                specialization.getDisplayName()
        );
    }
}
