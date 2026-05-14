package org.example.springappportfolio.dto;

import org.example.springappportfolio.models.Specialization;

public record PortfolioCardDto(
    Long portfolioId,
    Long userId,
    String username,
    String firstName,
    String lastName,
    String fullName,
    byte[] userImage,
    Specialization specialization,
    String specializationDisplayName,
    Integer experienceYears
) {
    public static PortfolioCardDto from(PortfolioDto dto) {
        String fullName = buildFullName(dto.firstName(), dto.lastName());

        return new PortfolioCardDto(
                dto.portfolioId(),
                dto.userId(),
                dto.username(),
                dto.firstName(),
                dto.lastName(),
                fullName,
                dto.userImage(),
                dto.specialization(),
                dto.specialization() != null ? dto.specialization().getDisplayName() : null,
                dto.experienceYears()
        );

    }

    private static String buildFullName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }

        return firstName != null ? firstName : (lastName != null ? lastName : null);
    }
}
