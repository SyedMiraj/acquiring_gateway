package com.prime.acquiring.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StanGenerator {

    private final AtomicInteger counter = new AtomicInteger(100000);

    /**
     * Generates a 6-digit STAN (System Trace Audit Number)
     * In real banking: resets daily per terminal/network
     */
    public String generate() {
        int value = counter.incrementAndGet();

        if (value > 999999) {
            counter.set(100000);
            value = counter.incrementAndGet();
        }

        return String.valueOf(value);
    }
}