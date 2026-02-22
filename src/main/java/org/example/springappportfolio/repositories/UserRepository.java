package org.example.springappportfolio.repositories;

import java.util.List;
import org.example.springappportfolio.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String username);

    User findUserById(Long id);
}
