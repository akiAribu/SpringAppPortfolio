package org.example.springappportfolio.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiException{

    public InternalServerErrorException(String userMessage) {
        super("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, userMessage);
    }

    public InternalServerErrorException(String userMessage, Throwable cause) {
        super("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, userMessage, cause);
    }
}
