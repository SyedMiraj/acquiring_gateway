package com.prime.acquiring.iso;

import com.prime.acquiring.jpa.entity.TransactionJournal;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Component;

@Component
public class IsoMessageBuilder {

    public ISOMsg build(TransactionJournal transactionJournal) {
        try {
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(new ISO87APackager());
            isoMsg.setMTI(transactionJournal.getMti());
            isoMsg.set(2, transactionJournal.getPan());
            isoMsg.set(3, transactionJournal.getProcessingCode());
            isoMsg.set(4, transactionJournal.getAmount());
            isoMsg.set(11, transactionJournal.getStan());
            isoMsg.set(37, transactionJournal.getRrn());
            isoMsg.set(41, transactionJournal.getTerminalId());
            isoMsg.set(42, transactionJournal.getMerchantId());
            isoMsg.set(49, "840");
            return isoMsg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}