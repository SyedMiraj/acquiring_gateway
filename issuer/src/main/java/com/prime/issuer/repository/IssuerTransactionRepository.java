package com.prime.acquiring.jpa.repository;

import com.prime.acquiring.jpa.entity.IssuerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssuerTransactionRepository extends JpaRepository<IssuerTransaction, Long> {
    Optional<IssuerTransaction>findByStanAndRrn(String stan, String rrn);
}
