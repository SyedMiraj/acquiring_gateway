package com.prime.acquiring.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stan;
    private String rrn;
    private String pan;
    private String amount;
    private String terminalId;
    private String processingCode;

    private String status;
    private String responseCode;

    private LocalDateTime createdAt;
}
