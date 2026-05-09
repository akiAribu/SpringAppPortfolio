package org.example.springappportfolio.dto;

public record ContactDto (

    Long contactId,
    String contactType,
    String contactValue,
    Integer displayOrder,
    Boolean isVisible

) {}
