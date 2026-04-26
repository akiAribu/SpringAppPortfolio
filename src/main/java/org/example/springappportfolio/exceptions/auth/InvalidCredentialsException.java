package org.example.springappportfolio.exceptions.auth;

public class InvalidCredentialsException extends AuthenticationException {
    public InvalidCredentialsException() {
        super("Invalid username/email or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
