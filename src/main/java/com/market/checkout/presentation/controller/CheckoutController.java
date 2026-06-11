package com.market.checkout.presentation.controller;

import com.market.checkout.domain.model.Transaction;
import com.market.checkout.domain.repository.TransactionRepository;
import com.market.checkout.infrastructure.messaging.KafkaTransactionProducer;
import com.market.checkout.presentation.dto.CheckoutRequest;
import com.market.checkout.presentation.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
    private final KafkaTransactionProducer producer;
    private final TransactionRepository repository;

    public CheckoutController(KafkaTransactionProducer producer, TransactionRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void processCheckout(@RequestBody CheckoutRequest request) {
        producer.sendTransaction(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String id) {
        return repository.findById(id)
                .map(t -> ResponseEntity.ok(mapToResponse(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private TransactionResponse mapToResponse(Transaction t) {
        var items = t.getItems().stream()
                .map(i -> new TransactionResponse.ItemResponse(i.productId(), i.quantity(), i.unitPrice(), i.getTotalPrice()))
                .toList();
        return new TransactionResponse(t.getId(), t.getStoreId(), t.getTerminalId(), t.getTotalAmount(), t.getStatus().name(), items);
    }
}
