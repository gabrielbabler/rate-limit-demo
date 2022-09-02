package com.gbabler.ratelimitdemo.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gbabler.ratelimitdemo.enumeration.SubscriptionType;
import com.gbabler.ratelimitdemo.exception.TooManyRequestsException;
import com.gbabler.ratelimitdemo.model.UserResponse;
import com.gbabler.ratelimitdemo.service.RateLimitService;
import com.gbabler.ratelimitdemo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RateLimitService rateLimitService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUsers(@RequestHeader String userId,
            @RequestHeader SubscriptionType subscriptionType) {
        rateLimitService.checkLimit(userId, subscriptionType);
        return userService.getUsers();
    }
}