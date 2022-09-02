package com.gbabler.ratelimitdemo.model;

import java.time.LocalDateTime;

import com.gbabler.ratelimitdemo.enumeration.SubscriptionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int totalCalls;
    private LocalDateTime lastCall;
    private SubscriptionType subscriptionType;

}