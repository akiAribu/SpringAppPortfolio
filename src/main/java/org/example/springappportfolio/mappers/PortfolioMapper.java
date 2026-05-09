package org.example.springappportfolio.mappers;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.dto.PortfolioDto;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.models.User;
import org.springframework.stereotype.Component;

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

        return new PortfolioDto(
                portfolio.getId(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserImage(),
                portfolio.getViewsCount(),
                portfolio.getIsPublic(),
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt(),
                contacts,
                // projects
                isOwner
        );

    }

    public PortfolioDto toDto(Portfolio portfolio) {
        return toDto(portfolio, false);
    }

}
