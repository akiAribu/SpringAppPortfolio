package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

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

//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false;
//        }
//
//        UserPrincipal principal = getUserPrincipal(authentication);
//        if (principal == null) {
//            return false;
//        }
//
//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
//        if(portfolio == null){
//            return false;
//        }
//
//        return portfolio.getUser().getId().equals(principal.getId());
    }

    public boolean isOwnerByUserId(Long userId, Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        return currentUserId != null &&
                currentUserId.equals(userId);

//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false;
//        }
//
//        UserPrincipal principal = getUserPrincipal(authentication);
//        if (principal == null) {
//            return false;
//        }
//
//        return principal.getId().equals(userId);
    }

    public boolean canAccessPortfolio(Long portfolioId, Authentication authentication) {

        return portfolioRepository.findById(portfolioId)
                .map(portfolio ->
                        isOwner(portfolioId, authentication) ||
                        Boolean.TRUE.equals(portfolio.getIsPublic()))
                .orElse(false);

//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElse(null);
//        if (portfolio == null){
//            return false;
//        }
//
//        if (isOwner(portfolioId, authentication)) {
//            return true;
//        }
//
//        return portfolio.getIsPublic() != null && portfolio.getIsPublic();

    }

    public boolean canAccessUserPortfolio(Long userId, Authentication authentication) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null || user.getPortfolio() == null) {
            return false;
        }

        Portfolio portfolio = user.getPortfolio();

        return isOwnerByUserId(userId, authentication) ||
                Boolean.TRUE.equals(portfolio.getIsPublic());

        //        User user = userRepository.findById(userId).orElse(null);
//        if (user == null || user.getPortfolio() == null) {
//            return false;
//        }
//
//        return canAccessPortfolio(user.getPortfolio().getId(), authentication);
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
