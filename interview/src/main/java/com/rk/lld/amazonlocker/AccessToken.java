package com.rk.lld.amazonlocker;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AccessToken {
    @Getter
    private String code;
    private Instant expiration;
    @Getter
    private Compartment compartment;

    public boolean isExpired() {
        return Instant.now().isAfter(expiration);
    }
}
