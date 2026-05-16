package com.prime.acquiring.engine;

import com.prime.acquiring.jpa.entity.TransactionJournal;
import com.prime.acquiring.model.TransactionStatus;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public TransactionJournal fromIso(ISOMsg msg) throws ISOException {
        return TransactionJournal.builder()
                .mti(msg.getMTI())
                .pan(msg.getString(2))
                .processingCode(msg.getString(3))
                .amount(msg.getString(4))
                .stan(msg.getString(11))
                .rrn(msg.getString(37))
                .terminalId(msg.getString(41))
                .merchantId(msg.getString(42))
                .status(TransactionStatus.RECEIVED)
                .responseCode(null)
                .reversed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}