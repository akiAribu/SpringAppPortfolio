package org.example.springappportfolio.mappers;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.dto.PortfolioDto;
import org.example.springappportfolio.dto.ProjectSummary;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.models.Project;
import org.example.springappportfolio.models.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PortfolioMapper {

    private final ContactMapper contactMapper;

    public PortfolioDto toDto(Portfolio portfolio, boolean isOwner) {
        if (portfolio == null) {
            return null;
        }

        User user = portfolio.getUser();

        List<ContactDto> contacts = portfolio.getContacts()
                .stream()
                .filter(contact -> isOwner || Boolean.TRUE.equals(contact.getIsVisible()))
                .map(contactMapper::toDto)
                .toList();

        List<ProjectSummary> projects = portfolio.getProjects()
                .stream()
                .filter(project -> isOwner || Boolean.TRUE.equals(project.getIsVisible()))
                .sorted(Comparator.comparing(Project::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(this::toProjectSummary)
                .toList();

        return new PortfolioDto(
                portfolio.getId(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserImage(),
                portfolio.getViewsCount(),
                portfolio.getIsPublic(),
                portfolio.getBio(),
                portfolio.getSpecialization(),
                portfolio.getExperienceYears(),
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt(),
                contacts,
                projects,
                isOwner
        );

    }

    private ProjectSummary toProjectSummary(Project project) {
        return new ProjectSummary(
                project.getId(),
                project.getProjectTitle(),
                project.getShortDescription(),
                project.getProjectPreview(),
                project.getIsVisible()
        );
    }

    public PortfolioDto toDto(Portfolio portfolio) {
        return toDto(portfolio, false);
    }

}
