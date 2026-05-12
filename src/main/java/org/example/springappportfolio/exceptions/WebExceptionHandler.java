package org.example.springappportfolio.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(NotFoundException e,  HttpServletRequest request) {
        log.warn("Resource not found : {}", e.getMessage());
        ModelAndView mav= new ModelAndView("error");
        mav.addObject("errorMessage", "The requested resource was not found.");
        mav.addObject("errorCode", "NOT_FOUND");

        return mav;

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        log.warn("Access denied: {}", e.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("errorCode", "ACCESS_DENIED");

        return mav;
    }

    @ExceptionHandler(ValidationException.class)
    public ModelAndView handleValidation(ValidationException e, HttpServletRequest request) {
        log.debug("Validation error: {}", e.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("errorCode", "VALIDATION_ERROR");

        return mav;
    }

    @ExceptionHandler(SecurityException.class)
    public ModelAndView handleSecurityException(SecurityException e, HttpServletRequest request) {
        log.warn("Security exception: {}", e.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", "Access denied. You don't have permission to view this page.");

        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred", e);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");

        return mav;
    }

}
