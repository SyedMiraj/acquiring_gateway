package com.prime.acquiring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsoRequest {

    /**
     * Primary Account Number
     */
    private String pan;

    /**
     * Processing Code
     * 000000 = Purchase
     * 310000 = Balance Inquiry
     */
    private String processingCode;

    /**
     * Transaction Amount
     * Example:
     * 000000001000
     */
    private String amount;

    /**
     * Terminal ID
     */
    private String terminalId;

    /**
     * Merchant ID
     */
    private String merchantId;

    /**
     * Currency Code
     * Example:
     * 840 = USD
     * 050 = BDT
     */
    private String currencyCode;

    /**
     * POS Entry Mode
     * Example:
     * 021 = Chip
     * 071 = Contactless
     */
    private String posEntryMode;
}