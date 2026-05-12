package org.example.springappportfolio.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdatePortfolioRequest {

    private String bio;

    private String specialization;

    @Min(value = 0, message = "Experience years can not be negative")
    private Integer experienceYears;

    private Boolean isPublic;
}
