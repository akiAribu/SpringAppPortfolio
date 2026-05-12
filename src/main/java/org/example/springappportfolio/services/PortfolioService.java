package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.dto.PortfolioDto;
import org.example.springappportfolio.dto.UpdatePortfolioRequest;
import org.example.springappportfolio.exceptions.AccessDeniedException;
import org.example.springappportfolio.exceptions.NotFoundException;
import org.example.springappportfolio.mappers.ContactMapper;
import org.example.springappportfolio.mappers.PortfolioMapper;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final PortfolioMapper portfolioMapper;

    public PortfolioDto getMyPortfolio(Authentication authentication) {

        Long userId = securityService.getCurrentUserId(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User", userId));

        return portfolioMapper.toDto(user.getPortfolio(), true);

    }

    public Long getPortfolioId(Authentication authentication) {
        Long userId = securityService.getCurrentUserId(authentication);

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId))
                .getPortfolio().getId();
    }

    public PortfolioDto getPortfolioById(Long portfolioId, Authentication authentication) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NotFoundException("Portfolio", portfolioId));;

        boolean isOwner = securityService.isOwner(portfolioId, authentication);
        boolean canAccess = securityService.canAccessPortfolio(portfolioId, authentication);

        if (!canAccess) {
            throw new AccessDeniedException("Access denied to private portfolio");
        }

        return portfolioMapper.toDto(portfolio, isOwner);

    }

    public PortfolioDto getPortfolioByUserId(Long userId, Authentication authentication) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));;

        if (user.getPortfolio() == null) {
            throw new NotFoundException("Portfolio not found");
        }

        boolean isOwner = securityService.isOwnerByUserId(userId, authentication);
        boolean canAccess = securityService.canAccessUserPortfolio(userId, authentication);

        if (!canAccess) {
            throw new AccessDeniedException("Access denied to private portfolio");
        }

        return portfolioMapper.toDto(user.getPortfolio(), isOwner);

    }

    public PortfolioDto updatePortfolio(Long userId, UpdatePortfolioRequest request, Authentication authentication) {

        if (!securityService.isOwnerByUserId(userId, authentication)) {
            throw new AccessDeniedException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        Portfolio portfolio = user.getPortfolio();

        if (portfolio == null) {
            throw new NotFoundException("Portfolio not found");
        }

        if (request.getBio() != null) {
            portfolio.setBio(request.getBio());
        }

        if (request.getSpecialization() != null) {
            portfolio.setSpecialization(request.getSpecialization());
        }

        if (request.getExperienceYears() != null) {
            portfolio.setExperienceYears(request.getExperienceYears());
        }

        if (request.getIsPublic() != null) {
            portfolio.setIsPublic(request.getIsPublic());
        }

        portfolio = portfolioRepository.save(portfolio);

        return portfolioMapper.toDto(portfolio, true);
    }

}
