package com.market.checkout.domain.repository;

import com.market.checkout.domain.model.Transaction;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction> findById(String id);
}
