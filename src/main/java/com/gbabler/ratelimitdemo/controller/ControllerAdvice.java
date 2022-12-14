package com.gbabler.ratelimitdemo.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gbabler.ratelimitdemo.exception.TooManyRequestsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleTooManyRequestsException(TooManyRequestsException ex, HttpServletResponse httpServletResponse) {
        log.error("Too many requests exception", ex);
        httpServletResponse.setHeader("Retry-After", ex.getRetryAfter());
    }
}