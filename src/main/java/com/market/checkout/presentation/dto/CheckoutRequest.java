package com.market.checkout.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutRequest(
        String transactionId,
        String storeId,
        String terminalId,
        List<ItemDto> items
) {
    public record ItemDto(String productId, int quantity, BigDecimal unitPrice) {}
}
