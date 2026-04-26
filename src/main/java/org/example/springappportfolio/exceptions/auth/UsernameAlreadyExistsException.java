package org.example.springappportfolio.exceptions.auth;

import org.example.springappportfolio.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiException {

    public UsernameAlreadyExistsException() {
        super("USERNAME_EXISTS", HttpStatus.CONFLICT, "Username already exists");
    }

    public UsernameAlreadyExistsException(String username) {
        super("USERNAME_EXISTS", HttpStatus.CONFLICT, "Username '" + username + "' already exists");
    }

}
