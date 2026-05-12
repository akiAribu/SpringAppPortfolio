package org.example.springappportfolio.exceptions;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiException{

    public AccessDeniedException(){
        super("ACCESS_DENIED", HttpStatus.FORBIDDEN, "Access Denied");
    }

    public AccessDeniedException(String message){
        super("ACCESS_DENIED", HttpStatus.FORBIDDEN, message);
    }

}
