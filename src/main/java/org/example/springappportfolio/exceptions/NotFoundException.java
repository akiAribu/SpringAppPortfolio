package org.example.springappportfolio.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException{

    public NotFoundException(String message){
        super("NOT_FOUND", HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(String resource, Long id){
        super("NOT_FOUND", HttpStatus.NOT_FOUND,
                String.format("%s not found with id: %d", resource, id));
    }

    public NotFoundException(String resource, Integer id) {
        super("NOT_FOUND", HttpStatus.NOT_FOUND,
                String.format("%s not found with id: %d", resource, id));
    }

    public NotFoundException(String resource, String identifier) {
        super("NOT_FOUND", HttpStatus.NOT_FOUND,
                String.format("%s not found: %s", resource, identifier));
    }

}
