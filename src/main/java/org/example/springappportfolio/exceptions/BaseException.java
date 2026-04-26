package org.example.springappportfolio.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException{
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] args;

    public BaseException(String errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = null;
    }

    public BaseException(String errorCode, HttpStatus httpStatus, String message, Object[] args) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public BaseException(String errorCode, HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = null;
    }
}
