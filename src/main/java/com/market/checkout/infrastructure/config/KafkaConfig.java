package com.market.checkout.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public static final String CHECKOUT_TOPIC = "supermarket-checkout-topic";

    @Bean
    public NewTopic checkoutTopic() {
        return TopicBuilder.name(CHECKOUT_TOPIC)
                .partitions(6)
                .replicas(1)
                .build();
    }
}
