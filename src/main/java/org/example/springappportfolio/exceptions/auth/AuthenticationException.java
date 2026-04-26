package org.example.springappportfolio.exceptions.auth;

import org.example.springappportfolio.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {

    public AuthenticationException(String message) {
        super("AUTH_ERROR", HttpStatus.UNAUTHORIZED, message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super("AUTH_ERROR", HttpStatus.UNAUTHORIZED, message, cause);
    }

}
