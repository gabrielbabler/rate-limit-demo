package com.gbabler.ratelimitdemo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gbabler.ratelimitdemo.enumeration.SubscriptionType;
import com.gbabler.ratelimitdemo.model.User;

@ExtendWith(MockitoExtension.class)
class RateLimitServiceTest {

    @InjectMocks
    private RateLimitService rateLimitService;

    @Mock
    private Map<String, User> cachedMap = new HashMap<>();

    @Test
    void shouldCheckLimitSuccessfullyForTheFirstTime() {
        String userId = "1";
        SubscriptionType subscriptionType = SubscriptionType.FREE_SUB;

        final User user = User.builder()
                .subscriptionType(subscriptionType)
                .totalCalls(1)
                .lastCall(LocalDateTime.now())
                .build();

        when(cachedMap.put(userId, user))
                .thenReturn(user);

        rateLimitService.checkLimit(userId, subscriptionType);

        final User user1 = cachedMap.get(userId);

        assertEquals(user.getLastCall(), user1.getLastCall());

    }

    private Map<String, User> cachedMap() {
        return new HashMap<>();
    }
}