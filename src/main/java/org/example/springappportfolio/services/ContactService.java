package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.dto.CreateContactRequest;
import org.example.springappportfolio.mappers.ContactMapper;
import org.example.springappportfolio.models.Contact;
import org.example.springappportfolio.models.Portfolio;
import org.example.springappportfolio.repositories.ContactRepository;
import org.example.springappportfolio.repositories.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final PortfolioRepository portfolioRepository;
    private final ContactMapper contactMapper;

    public List<ContactDto> getPortfolioContacts(Long portfolioId, boolean isOwner) {

        return contactRepository.findByPortfolioIdOrderByDisplayOrderAsc(portfolioId)
                .stream()
                .filter(contact ->
                        isOwner || Boolean.TRUE.equals(contact.getIsVisible()))
                .map(contactMapper::toDto)
                .toList();

    }

    @Transactional
    public void createContact(Long portfolioId, CreateContactRequest request, boolean isOwner) {

        validateOwnership(isOwner);

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() ->
                        new RuntimeException("Portfolio not found"));

        Contact contact = Contact.builder()
                .portfolio(portfolio)
                .contactType(request.contactType())
                .contactValue(request.contactValue())
                .displayOrder(
                        request.displayOrder() != null
                        ? request.displayOrder() : 0
                )
                .isVisible(
                        request.isVisible() != null
                        ? request.isVisible() : true
                )
                .build();

        contactRepository.save(contact);
        portfolio.getContacts().add(contact);

    }

    @Transactional
    public void deleteContact(Long portfolioId, Long contactId, boolean isOwner) {

        validateOwnership(isOwner);

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Contact contact = getPortfolioContact(portfolioId, contactId);

        contactRepository.delete(contact);
        portfolio.getContacts().remove(contact);

    }

    @Transactional
    public ContactDto updateContactVisability(Long portfolioId, Long contactId, Boolean isVisible, Boolean isOwner) {

        validateOwnership(isOwner);

        Contact contact = getPortfolioContact(portfolioId, contactId);
        contact.setIsVisible(isVisible);

        return contactMapper.toDto(contactRepository.save(contact));

    }

    private Contact getPortfolioContact(Long portfolioId, Long contactId) {

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact Not Found"));

        if (!contact.getPortfolio().getId().equals(portfolioId)) {
            throw new RuntimeException("Access denied");
        }

        return contact;
    }

    private void validateOwnership(Boolean isOwner) {
        if (!isOwner) {
            throw new SecurityException("Access denied");
        }

    }
}
