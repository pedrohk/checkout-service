package com.market.checkout.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class Transaction {
    private final String id;
    private final String storeId;
    private final String terminalId;
    private final List<Item> items;
    private final BigDecimal totalAmount;
    private final LocalDateTime timestamp;
    private TransactionStatus status;

    public Transaction(String id, String storeId, String terminalId, List<Item> items) {
        this.id = id;
        this.storeId = storeId;
        this.terminalId = terminalId;
        this.items = List.copyOf(items);
        this.totalAmount = calculateTotal(items);
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

    private BigDecimal calculateTotal(List<Item> items) {
        return items.stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markAsProcessed() {
        this.status = TransactionStatus.PROCESSED;
    }

    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getTerminalId() { return terminalId; }
    public List<Item> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionStatus getStatus() { return status; }
}
