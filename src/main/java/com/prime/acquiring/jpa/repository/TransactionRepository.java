package com.prime.acquiring.jpa.repository;

import com.prime.acquiring.jpa.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByStan(String stan);
}
