package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.ChangePasswordRequest;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.models.UserRole;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = findByUsername(username);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User changeRole(Long id, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("User not found"));
        UserRole newRole = UserRole.valueOf(roleName.toUpperCase());
        user.setUserRole(newRole);
        return userRepository.save(user);
    }

}
