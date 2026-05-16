package com.prime.acquiring.jpa.repository;

import com.prime.acquiring.jpa.entity.TransactionJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionJournalRepository extends JpaRepository<TransactionJournal, Long> {
    Optional<TransactionJournal> findByStan(String stan);
    Optional<TransactionJournal> findByStanAndRrn(String stan, String rrn);
}
