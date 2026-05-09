package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.dto.PortfolioDto;
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
                        new RuntimeException("User not found"));

        return portfolioMapper.toDto(user.getPortfolio(), true);

    }

    public Long getPortfolioId(Authentication authentication) {
        Long userId = securityService.getCurrentUserId(authentication);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getPortfolio().getId();
    }

    public PortfolioDto getPortfolioById(Long portfolioId, Authentication authentication) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        boolean isOwner = securityService.isOwner(portfolioId, authentication);
        boolean canAccess = securityService.canAccessPortfolio(portfolioId, authentication);

        if (!canAccess) {
            throw new SecurityException("Access denied to private portfolio");
        }

        return portfolioMapper.toDto(portfolio, isOwner);

    }

    public PortfolioDto getPortfolioByUserId(Long userId, Authentication authentication) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPortfolio() == null) {
            throw new RuntimeException("Portfolio not found");
        }

        boolean isOwner = securityService.isOwnerByUserId(userId, authentication);
        boolean canAccess = securityService.canAccessUserPortfolio(userId, authentication);

        if (!canAccess) {
            throw new SecurityException("Access denied to private portfolio");
        }

        return portfolioMapper.toDto(user.getPortfolio(), isOwner);

    }

    public PortfolioDto updatePortfolio(Long userId, Boolean isPublic, Authentication authentication) {

        if (!securityService.isOwnerByUserId(userId, authentication)) {
            throw new SecurityException("Access denied");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Portfolio portfolio = user.getPortfolio();

        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found");
        }

        if (isPublic != null) {
            portfolio.setIsPublic(isPublic);
        }

        portfolio = portfolioRepository.save(portfolio);

        return portfolioMapper.toDto(portfolio, true);
    }

}
