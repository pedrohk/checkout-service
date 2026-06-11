package com.market.checkout.infrastructure.messaging;

import com.market.checkout.CheckoutApplication;
import com.market.checkout.domain.repository.TransactionRepository;
import com.market.checkout.presentation.dto.CheckoutRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CheckoutApplication.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaMessagingTest {

    @Autowired
    private KafkaTransactionProducer producer;

    @MockitoBean
    private TransactionRepository repository;

    @Test
    void shouldProduceAndConsumeMessageCorrectly() {
        CheckoutRequest.ItemDto item = new CheckoutRequest.ItemDto("prod-abc", 5, BigDecimal.ONE);
        CheckoutRequest request = new CheckoutRequest("tx-test-99", "store-99", "term-99", List.of(item));

        producer.sendTransaction(request);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(repository, atLeastOnce()).save(any())
        );
    }
}