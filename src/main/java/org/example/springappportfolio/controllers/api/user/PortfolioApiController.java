package org.example.springappportfolio.controllers.api.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.dto.CreateContactRequest;
import org.example.springappportfolio.dto.PortfolioDto;
import org.example.springappportfolio.dto.UpdatePortfolioRequest;
import org.example.springappportfolio.repositories.UserRepository;
import org.example.springappportfolio.services.ContactService;
import org.example.springappportfolio.services.PortfolioService;
import org.example.springappportfolio.services.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PortfolioApiController {

    private final PortfolioService portfolioService;
    private final ContactService contactService;
    private final SecurityService securityService;

    @GetMapping("/portfolios/me")
    public ResponseEntity<PortfolioDto> getMyPortfolio(Authentication authentication) {

        return ResponseEntity.ok(
                portfolioService.getMyPortfolio(authentication)
        );

    }

    @PutMapping("/portfolios/me")
    public ResponseEntity<PortfolioDto> updateMyPortfolio(
            @Valid @RequestBody UpdatePortfolioRequest request,
            Authentication authentication
    ) {
        Long userId = securityService.getCurrentUserId(authentication);

        PortfolioDto portfolio = portfolioService.updatePortfolio(
                userId,
                request,
                authentication
        );

        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/portfolios/{portfolioId}")
    public ResponseEntity<PortfolioDto> getPortfolioById(
            @PathVariable Long portfolioId,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                portfolioService.getPortfolioById(portfolioId, authentication)
        );

    }

    @GetMapping("/user/{userId}/portfolio")
    public ResponseEntity<PortfolioDto> getUserPortfolio(
            @PathVariable Long userId,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                portfolioService.getPortfolioByUserId(userId, authentication)
        );

    }

    @GetMapping("/portfolios/me/contacts")
    public ResponseEntity<List<ContactDto>> getMyContacts(Authentication authentication) {

        return ResponseEntity.ok(
                portfolioService.getMyPortfolio(authentication).contacts()
        );

    }

    @PostMapping("/contacts")
    public ResponseEntity<Void> createContact(
            @RequestBody CreateContactRequest request,
            Authentication authentication
    ) {

        Long portfolioId = portfolioService.getPortfolioId(authentication);

        contactService.createContact(
                portfolioId,
                request,
                true
        );

        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Long contactId,
            Authentication authentication
    ) {

        Long portfolioId = portfolioService.getPortfolioId(authentication);

        contactService.deleteContact(
                portfolioId,
                contactId,
                true
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/contacts/{contactId}/visibility")
    public ResponseEntity<ContactDto> updateContactVisibility(
            @PathVariable Long contactId,
            @RequestParam Boolean isVisible,
            Authentication authentication
    ) {

        Long portfolioId = portfolioService.getPortfolioId(authentication);

        return ResponseEntity.ok(
                contactService.updateContactVisability(
                        portfolioId,
                        contactId,
                        isVisible,
                        true
                )
        );

    }

}
