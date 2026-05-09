package org.example.springappportfolio.repositories;

import org.example.springappportfolio.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByPortfolioIdOrderByDisplayOrderAsc(Long portfolioId);

}
