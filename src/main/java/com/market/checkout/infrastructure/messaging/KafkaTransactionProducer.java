package com.market.checkout.infrastructure.messaging;

import com.market.checkout.infrastructure.config.KafkaConfig;
import com.market.checkout.presentation.dto.CheckoutRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionProducer {
    private final KafkaTemplate<String, CheckoutRequest> kafkaTemplate;

    public KafkaTransactionProducer(KafkaTemplate<String, CheckoutRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(CheckoutRequest request) {
        String partitionKey = request.storeId() + "-" + request.terminalId();
        kafkaTemplate.send(KafkaConfig.CHECKOUT_TOPIC, partitionKey, request);
    }
}
