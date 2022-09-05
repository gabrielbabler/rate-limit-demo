package com.gbabler.ratelimitdemo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionType {

    FREE_SUB(1, 5), PAY_SUB(5, 10);

    private final int requestLimit;
    private final int timeLimitInSeconds;

}