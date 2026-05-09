package org.example.springappportfolio.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateContactRequest (

    @NotBlank(message = "Contact type is required")
    String contactType,

    @NotBlank(message =  "Contact value is required")
    String contactValue,

    Integer displayOrder,

    Boolean isVisible

) {}
