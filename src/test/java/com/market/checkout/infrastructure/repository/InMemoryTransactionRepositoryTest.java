package com.market.checkout.infrastructure.repository;

import com.market.checkout.domain.model.Item;
import com.market.checkout.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.market.checkout.infrastructure.repository.InMemoryTransactionRepository;

class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
    }

    @Test
    void shouldSaveAndFindTransactionById() {
        Item item = new Item("p1", 1, BigDecimal.TEN);
        Transaction transaction = new Transaction("t1", "s1", "term1", List.of(item));

        repository.save(transaction);
        Optional<Transaction> result = repository.findById("t1");

        assertTrue(result.isPresent());
        assertEquals("t1", result.get().getId());
        assertEquals("s1", result.get().getStoreId());
    }

    @Test
    void shouldReturnEmptyWhenTransactionDoesNotExist() {
        Optional<Transaction> result = repository.findById("non-existent");
        assertTrue(result.isEmpty());
    }
}
