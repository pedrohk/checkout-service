package com.market.checkout.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

public record TransactionResponse(
        String transactionId,
        String storeId,
        String terminalId,
        BigDecimal totalAmount,
        String status,
        List<ItemResponse> items
) {
    public record ItemResponse(String productId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {}
}
