package org.example.springappportfolio.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends BaseException {

    public ApiException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

    public ApiException(String errorCode, HttpStatus httpStatus, String message, Object[] args) {
        super(errorCode, httpStatus, message, args);
    }

    public ApiException(String errorCode, HttpStatus httpStatus, String message, Throwable cause) {
        super(errorCode, httpStatus, message, cause);
    }
}