package org.example.springappportfolio.repositories;

import org.example.springappportfolio.models.Contact;
import org.example.springappportfolio.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>, JpaSpecificationExecutor<Portfolio> {

    @Query("""
        SELECT DISTINCT p
        FROM Portfolio p
        LEFT JOIN FETCH p.user
        LEFT JOIN FETCH p.contacts
        LEFT JOIN FETCH p.projects
        WHERE p.id = :portfolioId
    """)
    Optional<Portfolio> findByIdWithRelations(long id);

    @Query("""
        SELECT c 
        FROM Contact c 
        WHERE c.portfolio.id = :portfolioId 
        ORDER BY c.displayOrder ASC
    """)
    List<Contact> findContactsByPortfolioId(Long portfolioId);

    @Query("""
        SELECT COUNT(p)
        FROM Portfolio p 
        WHERE p.isPublic = true
    """)
    long countPublicPortfolios();
}
