package com.prime.acquiring.model;

import lombok.Data;

@Data
public class IsoRequest {
    private String pan;
    private String amount;
    private String terminalId;
    private String processingCode;
}
