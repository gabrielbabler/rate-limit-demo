package com.gbabler.ratelimitdemo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TooManyRequestsException extends RuntimeException {
    private String retryAfter;
}
