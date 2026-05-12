package org.example.springappportfolio.dto;

public record ProjectSummary (
   Integer projectId,
   String title,
   String shortDescription,
   byte[] preview,
   Boolean isVisible
) {}
