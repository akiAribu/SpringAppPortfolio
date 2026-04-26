package org.example.springappportfolio.exceptions;

import org.example.springappportfolio.dto.ApiErrorResponse;
import org.example.springappportfolio.exceptions.auth.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyExists(ApiException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .message(e.getMessage())
                        .status(e.getHttpStatus().value())
                        .timestamp(Instant.now())
                        .build()
                );
    }

}
