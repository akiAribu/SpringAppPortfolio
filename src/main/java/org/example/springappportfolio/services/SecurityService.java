package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public boolean isAdmin(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

    }

    public boolean isOwner(Long portfolioId, Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        if (currentUserId == null) {
            return false;
        }

        return portfolioRepository.findById(portfolioId)
                .map(portfolio ->
                        portfolio.getUser()
                                .getId()
                                .equals(currentUserId))
                .orElse(false);
    }

    public boolean isOwnerByUserId(Long userId, Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return currentUserId != null &&
                currentUserId.equals(userId);
    }

    public boolean canAccessPortfolio(Long portfolioId, Authentication authentication) {

        return portfolioRepository.findById(portfolioId)
                .map(portfolio ->
                        isOwner(portfolioId, authentication) ||
                        Boolean.TRUE.equals(portfolio.getIsPublic()) ||
                        isAdmin(authentication))
                .orElse(false);
    }

    public boolean canAccessUserPortfolio(Long userId, Authentication authentication) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null || user.getPortfolio() == null) {
            return false;
        }

        Portfolio portfolio = user.getPortfolio();

        return isOwnerByUserId(userId, authentication) ||
                Boolean.TRUE.equals(portfolio.getIsPublic() ||
                        isAdmin(authentication));
    }

    public Long getCurrentUserId(Authentication authentication) {

        UserPrincipal principal = getUserPrincipal(authentication);

        return principal != null
                ? principal.getId()
                : null;

    }

    private UserPrincipal getUserPrincipal(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        }

        return null;
    }

}
