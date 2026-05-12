package org.example.springappportfolio.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.example.springappportfolio.dto.ApiErrorResponse;
import org.example.springappportfolio.dto.ValidationError;
import org.example.springappportfolio.exceptions.auth.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "org.example.springappportfolio.controllers.api")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException e) {
        log.warn("Resource not found : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .message(e.getMessage())
                        .status(404)
                        .timestamp(Instant.now())
                        .build()
                );

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .message(e.getMessage())
                        .status(403)
                        .timestamp(Instant.now())
                        .build()
                );

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(ValidationException e) {
        log.debug("Validation error : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .message(e.getMessage())
                        .status(400)
                        .timestamp(Instant.now())
                        .errors(e.getErrors())
                        .build()
                );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiErrorResponse> handleInternalServerError(InternalServerErrorException e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .message("An internal error occurred. Please try again later.")
                        .status(500)
                        .timestamp(Instant.now())
                        .build()
                );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException e) {
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        String message = errors.stream()
                .map(error -> error.getField() + ": " + error.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .errorCode("VALIDATION_ERROR")
                        .message(message)
                        .status(400)
                        .timestamp(Instant.now())
                        .errors(errors)
                        .build()
                );

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.debug("Invalid argument : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .errorCode("VALIDATION_ERROR")
                        .message(e.getMessage())
                        .status(400)
                        .timestamp(Instant.now())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception e) {
        log.debug("Unexpected error occurred", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.builder()
                        .errorCode("INTERNAL_SERVER_ERROR")
                        .message("An unexpected error occurred. Please try again later.")
                        .status(500)
                        .timestamp(Instant.now())
                        .build()
                );
    }

}
