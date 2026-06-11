package com.market.checkout.domain.model;

import java.math.BigDecimal;

public record Item(String productId, int quantity, BigDecimal unitPrice) {
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
