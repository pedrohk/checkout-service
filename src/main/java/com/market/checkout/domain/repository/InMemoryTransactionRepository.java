package com.market.checkout.infrastructure.repository;

import com.market.checkout.domain.model.Transaction;
import com.market.checkout.domain.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<String, Transaction> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        storage.put(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}
