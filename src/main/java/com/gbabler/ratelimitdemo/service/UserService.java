package com.gbabler.ratelimitdemo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gbabler.ratelimitdemo.model.UserResponse;

@Service
public class UserService {

    public List<UserResponse> getUsers() {
        return Arrays.asList(new UserResponse("Gabriel"), new UserResponse("Babler"));
    }
}