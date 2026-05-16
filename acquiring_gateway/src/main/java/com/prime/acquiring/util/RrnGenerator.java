package com.prime.acquiring.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RrnGenerator {

    private final AtomicLong counter = new AtomicLong(100000);

    /**
     * Generates a 12-13 digit RRN
     * Format: YYMMDD + sequence
     */
    public String generate() {

        String date = new SimpleDateFormat("yyMMdd").format(new Date());
        long sequence = counter.incrementAndGet();

        if (sequence > 999999) {
            counter.set(100000);
            sequence = counter.incrementAndGet();
        }

        return date + String.format("%06d", sequence);
    }
}
