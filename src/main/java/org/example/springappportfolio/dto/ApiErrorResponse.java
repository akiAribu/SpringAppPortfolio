package org.example.springappportfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiErrorResponse {
    private String errorCode;
    private String message;
    private int status;
    private Instant timestamp;
}
