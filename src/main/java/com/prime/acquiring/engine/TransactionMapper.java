package com.prime.acquiring.engine;

import com.prime.acquiring.jpa.entity.Transaction;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public Transaction fromIso(ISOMsg msg) {

        return Transaction.builder()
                .stan(msg.getString(11))
                .pan(msg.getString(2))
                .amount(msg.getString(4))
                .terminalId(msg.getString(41))
                .processingCode(msg.getString(3))
                .createdAt(LocalDateTime.now())
                .build();
    }
}