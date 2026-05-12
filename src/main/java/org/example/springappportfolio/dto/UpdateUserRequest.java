package org.example.springappportfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
   @NotBlank (message = "Username can not be empty")
   private String username;

   @NotBlank (message = "Email can not be empty")
   @Email (message = "Invalid email format")
   private String email;

   private String firstName;

   private String lastName;

}
