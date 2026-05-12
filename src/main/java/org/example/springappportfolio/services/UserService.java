package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.dto.*;
import org.example.springappportfolio.exceptions.InternalServerErrorException;
import org.example.springappportfolio.exceptions.ValidationException;
import org.example.springappportfolio.exceptions.auth.EmailAlreadyExistsException;
import org.example.springappportfolio.exceptions.auth.UsernameAlreadyExistsException;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.models.UserRole;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public User findByIdWithPortfolio(Long id) {

        return userRepository.findByIdWithPortfolio(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

    }

    @Transactional
    public void updateUser(Long userId, UpdateUserRequest request) {

        User user = findById(userId);

        validateUsernameUniqueness(
                request.getUsername(),
                user.getId()
        );

        validateEmailUniqueness(
                request.getEmail(),
                user.getId()
        );

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        updateAuthenticationPrincipal(user);

    }

    @Transactional
    public void updateUserAvatar(Long userId, MultipartFile file) {

        User user = findById(userId);

        validateAvatar(file);

        try {
            user.setUserImage(file.getBytes());
            user.setImageType(file.getContentType());
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update avatar", e);
        }
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {

        User user = findById(userId);

        validateOldPassword(user, request.oldPassword());
        validateNewPassword(user, request.newPassword());

        user.setUserPassword(passwordEncoder.encode(request.newPassword()));

    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    @Transactional
    public User changeRole(Long userId, String roleName) {
        User user = findById(userId);

        UserRole newRole = UserRole.valueOf(roleName.toUpperCase());

        user.setUserRole(newRole);

        return userRepository.save(user);
    }

    public void validateUsernameUniqueness(String username, Long currentUserId) {

        userRepository.findByUsername(username)
                .filter(user -> currentUserId == null || !user.getId().equals(currentUserId))
                .ifPresent(user -> {
                    throw new UsernameAlreadyExistsException(username);
                });

    }

    public void validateUsernameUniqueness(String username) {
        validateUsernameUniqueness(username, null);
    }

    public void validateEmailUniqueness(String email, Long currentUserId) {
        userRepository.findByEmail(email)
                .filter(user -> currentUserId == null || !user.getId().equals(currentUserId))
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException(email);
                });
    }

    public void validateEmailUniqueness(String email) {
        validateEmailUniqueness(email, null);
    }

    public void validateAvatar(MultipartFile file) {

        ValidationException exception = new ValidationException("Avatar validation failed");

        if (file.isEmpty()) {
            exception.addError("file", "File is empty");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            exception.addError("file", "File is too large (max 2MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png"))) {
           exception.addError("file", "Invalid file type. Allowed: jpeg, png");
        }

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

    }

    private void validateOldPassword(User user, String oldPassword) {

        if (!passwordEncoder.matches(
                oldPassword,
                user.getUserPassword())
        ) {
            ValidationException e = new ValidationException("Password validation failed");
            e.addError("oldPassword", "Old password is incorrect");
            throw e;
        }

    }

    private void validateNewPassword(User user, String newPassword) {

        if (passwordEncoder.matches(
                newPassword,
                user.getUserPassword())
        ) {
            ValidationException e = new ValidationException("Password validation failed");
            e.addError("newPassword", "New password must be different from current");
            throw e;
        }

    }

    private void updateAuthenticationPrincipal(User user) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal()
                        instanceof UserPrincipal oldPrincipal)) {

            return;

        }

        UserPrincipal updatedPrincipal =
                new UserPrincipal(
                        oldPrincipal.getId(),
                        user.getUsername(),
                        oldPrincipal.getPassword(),
                        oldPrincipal.getAuthorities()
                );

        Authentication newAuthentication =
                new UsernamePasswordAuthenticationToken(
                        updatedPrincipal,
                        null,
                        updatedPrincipal.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(newAuthentication);

    }
}
