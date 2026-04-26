package org.example.springappportfolio.exceptions.auth;

import org.example.springappportfolio.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException() {
        super("EMAIL_EXISTS", HttpStatus.CONFLICT, "Email already registered");
    }

    public EmailAlreadyExistsException(String email) {
        super("EMAIL_EXISTS", HttpStatus.CONFLICT, "Email '" + email + "'already registered");
    }
}
