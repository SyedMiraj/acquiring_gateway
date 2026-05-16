package com.prime.acquiring.jpa.entity;

import com.prime.acquiring.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_journal")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mti;
    private String pan;
    private String processingCode;
    private String amount;
    private String stan;
    private String rrn;
    private String terminalId;
    private String merchantId;
    private String responseCode;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private Boolean reversed = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
