package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.*;
import org.example.springappportfolio.exceptions.AccessDeniedException;
import org.example.springappportfolio.exceptions.NotFoundException;
import org.example.springappportfolio.mappers.PortfolioMapper;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.example.springappportfolio.repositories.UserRepository;
import org.example.springappportfolio.specifications.PortfolioSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public PortfolioPageResponse searchPortfoliosPaginated(PortfolioSearchRequest request) {
        var spec = PortfolioSpecifications.isPublic()
                .and(PortfolioSpecifications.excludeAdmins());

        if (request.specialization() != null) {
            spec = spec.and(PortfolioSpecifications.hasSpecialization(request.specialization()));
        }
        if (request.specializations() != null) {
            spec = spec.and(PortfolioSpecifications.hasAnySpecialization(request.specializations()));
        }
        if (request.minExperience() != null) {
            spec = spec.and(PortfolioSpecifications.hasMinExperience(request.minExperience()));
        }
        if (request.maxExperience() != null) {
            spec = spec.and(PortfolioSpecifications.hasMaxExperience(request.maxExperience()));
        }

        if (request.experienceRanges() != null &&  !request.experienceRanges().isEmpty()) {
            spec = spec.and(PortfolioSpecifications.hasExperienceInRanges(request.experienceRanges()));
        }

        if (request.tags() != null && !request.tags().isEmpty()) {
            spec = spec.and(PortfolioSpecifications.hasAnyTag(request.tags()));
        }

        if (request.q() != null && !request.q().isBlank()) {
            spec = spec.and(PortfolioSpecifications.hasNameContaining(request.q()));
        }

        int page = request.page() != null ? request.page() : 0;
        int size = request.size() != null ? request.size() : 8;

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Portfolio> portfolioPage = portfolioRepository.findAll(spec, pageRequest);

        List<PortfolioCardDto> content = portfolioPage.getContent().stream()
                .map(p -> PortfolioCardDto.from(portfolioMapper.toDto(p, false)))
                .toList();

        return new PortfolioPageResponse(
                content,
                portfolioPage.getNumber(),
                portfolioPage.getSize(),
                portfolioPage.getTotalElements(),
                portfolioPage.getTotalPages(),
                portfolioPage.hasNext(),
                portfolioPage.hasPrevious()
        );

    }

    public List<PortfolioDto> searchPortfolios(PortfolioSearchRequest request) {
        var spec = PortfolioSpecifications.isPublic()
                .and(PortfolioSpecifications.excludeAdmins());

        if (request.specialization() != null) {
            spec = spec.and(PortfolioSpecifications.hasSpecialization(request.specialization()));
        }

        if (request.specializations() != null && !request.specializations().isEmpty()) {
            spec = spec.and(PortfolioSpecifications.hasAnySpecialization(request.specializations()));
        }

        if (request.minExperience() != null) {
            spec = spec.and(PortfolioSpecifications.hasMinExperience(request.minExperience()));
        }

        if (request.maxExperience() != null) {
            spec = spec.and(PortfolioSpecifications.hasMaxExperience(request.maxExperience()));
        }

        if (request.experienceRanges() != null && !request.experienceRanges().isEmpty()) {
            spec = spec.and(PortfolioSpecifications.hasExperienceInRanges(request.experienceRanges()));
        }

        if (request.tags() != null && !request.tags().isEmpty()) {
            spec = spec.and(PortfolioSpecifications.hasAnyTag(request.tags()));
        }

        if (request.q() != null && !request.q().isBlank()) {
            spec = spec.and(PortfolioSpecifications.hasNameContaining(request.q()));
        }

        List<Portfolio> portfolios = portfolioRepository.findAll(spec);

        return portfolios.stream()
                .map(p -> portfolioMapper.toDto(p, false))
                .toList();
    }

}
