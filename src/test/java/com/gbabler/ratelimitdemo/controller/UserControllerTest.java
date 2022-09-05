package com.gbabler.ratelimitdemo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.gbabler.ratelimitdemo.enumeration.SubscriptionType;
import com.gbabler.ratelimitdemo.exception.TooManyRequestsException;
import com.gbabler.ratelimitdemo.model.User;
import com.gbabler.ratelimitdemo.model.UserResponse;
import com.gbabler.ratelimitdemo.service.RateLimitService;
import com.gbabler.ratelimitdemo.service.UserService;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    private static final String USERS_ENDPOINT = "/users";

    @MockBean
    private UserService userService;

    @MockBean
    private RateLimitService rateLimitService;

    @Autowired
    private MockMvc mockMvc;

    private List<UserResponse> users;

    @BeforeEach
    void generateUsers() {
        users = Arrays.asList(new UserResponse("Gabriel"), new UserResponse("Babler"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        doNothing()
                .when(rateLimitService).checkLimit("1", SubscriptionType.FREE_SUB);
        when(userService.getUsers())
                .thenReturn(users);

        mockMvc.perform(get(USERS_ENDPOINT)
                        .header("userId", "1")
                        .header("subscriptionType", SubscriptionType.FREE_SUB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Gabriel")))
                .andExpect(jsonPath("$[1].name", is("Babler")));

        verify(userService, times(1)).getUsers();
        verify(rateLimitService, times(1)).checkLimit("1", SubscriptionType.FREE_SUB);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(rateLimitService);
    }

    @Test
    void shouldNotGetAllUsersDueToLimitExceed() throws Exception {
        doThrow(new TooManyRequestsException("4"))
                .when(rateLimitService).checkLimit("1", SubscriptionType.FREE_SUB);

        mockMvc.perform(get(USERS_ENDPOINT)
                        .header("userId", "1")
                        .header("subscriptionType", SubscriptionType.FREE_SUB))
                .andExpect(status().is4xxClientError())
                .andExpect(header().string("Retry-After", "4"));

        verify(rateLimitService, times(1)).checkLimit("1", SubscriptionType.FREE_SUB);
        verifyNoMoreInteractions(rateLimitService);
        verifyNoInteractions(userService);
    }
}