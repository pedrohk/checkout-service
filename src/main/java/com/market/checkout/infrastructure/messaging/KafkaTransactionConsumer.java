package com.market.checkout.infrastructure.messaging;

import com.market.checkout.domain.model.Item;
import com.market.checkout.domain.model.Transaction;
import com.market.checkout.domain.repository.TransactionRepository;
import com.market.checkout.infrastructure.config.KafkaConfig;
import com.market.checkout.presentation.dto.CheckoutRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaTransactionConsumer {
    private final TransactionRepository repository;

    public KafkaTransactionConsumer(TransactionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = KafkaConfig.CHECKOUT_TOPIC, groupId = "checkout-group")
    public void consumeTransaction(CheckoutRequest request) {
        List<Item> domainItems = request.items().stream()
                .map(i -> new Item(i.productId(), i.quantity(), i.unitPrice()))
                .toList();

        Transaction transaction = new Transaction(
                request.transactionId(),
                request.storeId(),
                request.terminalId(),
                domainItems
        );

        transaction.markAsProcessed();
        repository.save(transaction);
    }
}
