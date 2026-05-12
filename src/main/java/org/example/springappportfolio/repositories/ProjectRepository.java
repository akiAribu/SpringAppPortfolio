package org.example.springappportfolio.repositories;

import org.example.springappportfolio.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByPortfolioId(Long portfolioId);
    List<Project> findByPortfolioIdOrderByCreatedAtDesc(Long portfolioId);

}
