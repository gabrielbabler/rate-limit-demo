package com.gbabler.ratelimitdemo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gbabler.ratelimitdemo.enumeration.SubscriptionType;
import com.gbabler.ratelimitdemo.exception.TooManyRequestsException;
import com.gbabler.ratelimitdemo.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final Map<String, User> cachedMap = new HashMap<>();

    public void checkLimit(String userId, SubscriptionType subscriptionType) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("America/Sao_Paulo"));
        final String now = LocalDateTime.now().format(dateTimeFormatter);
        final LocalDateTime nowDateTime = LocalDateTime.parse(now);

        if(cachedMap.containsKey(userId)) {
            final User user = cachedMap.get(userId);

            final int timeLimit = user.getSubscriptionType().getTimeLimit();

            final String lastCallString = user.getLastCall()
                    .format(dateTimeFormatter);

            final LocalDateTime lastCall = LocalDateTime.parse(lastCallString);

            final long timeBetween = ChronoUnit.SECONDS.between(lastCall, nowDateTime);

            if(timeBetween > timeLimit) {
                user.setTotalCalls(0);
            }

            final int requestLimit = user.getSubscriptionType().getRequestLimit();

            if(requestLimit == user.getTotalCalls()) {
                throw new TooManyRequestsException(String.valueOf(timeLimit - timeBetween));
            } else {
                user.setLastCall(nowDateTime);
                user.setTotalCalls(user.getTotalCalls() + 1);
                cachedMap.put(userId, user);
            }
        } else {
            final User user = User.builder()
                    .subscriptionType(subscriptionType)
                    .totalCalls(1)
                    .lastCall(nowDateTime)
                    .build();

            cachedMap.put(userId, user);
            log.info("First call for userId: {} with subscription {}", userId,
                    user.getSubscriptionType());
        }
    }
}