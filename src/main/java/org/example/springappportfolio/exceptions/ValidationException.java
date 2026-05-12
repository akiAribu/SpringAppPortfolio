package org.example.springappportfolio.exceptions;

import lombok.Getter;
import org.example.springappportfolio.dto.ValidationError;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends ApiException {

    private final List<ValidationError> errors = new ArrayList<>();

    public ValidationException(String message) {
        super("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, message);
    }

    public ValidationException(String message, List<ValidationError> errors) {
        super("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, message);
        this.errors.addAll(errors);
    }

    public void addError(String field, String message) {
        this.errors.add(new ValidationError(field, message));
    }

}
