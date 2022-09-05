package com.gbabler.ratelimitdemo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TooManyRequestsException extends RuntimeException {
    private final String retryAfter;
}
