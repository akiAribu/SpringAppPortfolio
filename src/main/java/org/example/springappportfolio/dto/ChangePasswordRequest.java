package org.example.springappportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest (

    @NotBlank(message = "Old password can not be empty")
    String oldPassword,

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    String newPassword

) {}
