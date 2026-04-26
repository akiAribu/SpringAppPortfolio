package org.example.springappportfolio.dto;

import lombok.Builder;
import lombok.Data;
import org.example.springappportfolio.models.UserRole;

import java.time.Instant;

@Builder
@Data
public class AuthResponse {
    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private Instant createdAt;
    private String message; // сообщение об успешности регистрации или авторизации
}
