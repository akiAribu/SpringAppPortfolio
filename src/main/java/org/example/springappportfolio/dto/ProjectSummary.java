package org.example.springappportfolio.dto;

public record ProjectSummary (
   Long projectId,
   String title,
   String shortDescription,
   byte[] preview,
   Boolean isVisible
) {}
