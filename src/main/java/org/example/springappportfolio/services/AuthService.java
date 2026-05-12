package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.dto.AuthResponse;
import org.example.springappportfolio.dto.LoginRequest;
import org.example.springappportfolio.dto.RegisterRequest;
import org.example.springappportfolio.exceptions.auth.EmailAlreadyExistsException;
import org.example.springappportfolio.exceptions.auth.InvalidCredentialsException;
import org.example.springappportfolio.exceptions.auth.UsernameAlreadyExistsException;
import org.example.springappportfolio.models.Portfolio;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        userService.validateUsernameUniqueness(request.getUsername());
        userService.validateEmailUniqueness(request.getEmail());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.ROLE_USER)
                .build();

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setViewsCount(0);

        user.setPortfolio(portfolio);
        userRepository.save(user);

        return new AuthResponse (
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole(),
                user.getCreatedAt(),
                "Registration successful"

        );

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

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        UserRole role = userRepository.findById(principal.getId())
                .map(User::getUserRole)
                .orElseThrow(InvalidCredentialsException::new);

        return new AuthResponse(
                principal.getId(),
                principal.getUsername(),
                null,
                role,
                null,
                "Login Successful"
        );

    }

}