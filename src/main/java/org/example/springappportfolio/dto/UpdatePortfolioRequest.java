package org.example.springappportfolio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.springappportfolio.models.Specialization;

@Data
public class UpdatePortfolioRequest {

    @Size(max = 500, message = "Bio must be less then 500")
    private String bio;

    private Specialization specialization;

    @Min(value = 0, message = "Experience years can not be negative")
    private Integer experienceYears;

    private Boolean isPublic;
}
