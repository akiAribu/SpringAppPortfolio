package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.AuthResponse;
import org.example.springappportfolio.dto.LoginRequest;
import org.example.springappportfolio.dto.RegisterRequest;
import org.example.springappportfolio.exceptions.auth.EmailAlreadyExistsException;
import org.example.springappportfolio.exceptions.auth.InvalidCredentialsException;
import org.example.springappportfolio.exceptions.auth.UsernameAlreadyExistsException;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.models.UserRole;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Проверка уникальности имени пользователя
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        // Проверка уникальности email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getUserRole())
                .message("Registration successful")
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new InvalidCredentialsException());

        return mapToAuthResponse(user, "Login successful");
    }

    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return mapToAuthResponse(user, null);
    }

    private AuthResponse mapToAuthResponse(User user, String message) {
        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getUserRole())
                .createdAt(user.getCreatedAt())
                .message(message)
                .build();
    }
}