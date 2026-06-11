package com.market.checkout.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void shouldCreateTransactionAndCalculateTotalAmount() {
        Item item1 = new Item("prod-1", 2, new BigDecimal("10.50"));
        Item item2 = new Item("prod-2", 1, new BigDecimal("5.00"));
        Transaction transaction = new Transaction("tx-123", "store-5", "term-9", List.of(item1, item2));

        assertNotNull(transaction.getId());
        assertEquals("tx-123", transaction.getId());
        assertEquals("store-5", transaction.getStoreId());
        assertEquals("term-9", transaction.getTerminalId());
        assertEquals(2, transaction.getItems().size());
        assertEquals(new BigDecimal("26.00"), transaction.getTotalAmount());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertNotNull(transaction.getTimestamp());
    }

    @Test
    void shouldMarkTransactionAsProcessed() {
        Item item = new Item("prod-1", 1, new BigDecimal("10.00"));
        Transaction transaction = new Transaction("tx-123", "store-1", "term-1", List.of(item));

        transaction.markAsProcessed();

        assertEquals(TransactionStatus.PROCESSED, transaction.getStatus());
    }

    @Test
    void shouldCalculateTotalPriceForSingleItem() {
        Item item = new Item("prod-1", 3, new BigDecimal("2.50"));
        assertEquals(new BigDecimal("7.50"), item.getTotalPrice());
    }
}
