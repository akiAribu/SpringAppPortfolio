package org.example.springappportfolio.repositories;

import java.util.Optional;

import org.example.springappportfolio.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u 
        FROM User u 
        WHERE u.username = :login OR u.email = :login
    """)
    Optional<User> findByUsernameOrEmail(@Param("login") String login);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.portfolio p
        LEFT JOIN FETCH p.contacts
        LEFT JOIN FETCH p.projects
        WHERE u.id = :userId
    """)
    Optional<User> findByIdWithPortfolio(Long userId);
}