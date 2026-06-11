package com.market.checkout.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.checkout.CheckoutApplication;
import com.market.checkout.domain.model.Item;
import com.market.checkout.domain.model.Transaction;
import com.market.checkout.domain.repository.TransactionRepository;
import com.market.checkout.infrastructure.messaging.KafkaTransactionProducer;
import com.market.checkout.presentation.dto.CheckoutRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
@ContextConfiguration(classes = CheckoutApplication.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KafkaTransactionProducer producer;

    @MockitoBean
    private TransactionRepository repository;

    @Test
    void shouldAcceptCheckoutRequestAndReturn202() throws Exception {
        CheckoutRequest.ItemDto itemDto = new CheckoutRequest.ItemDto("p1", 2, BigDecimal.TEN);
        CheckoutRequest request = new CheckoutRequest("tx-1", "store-1", "term-1", List.of(itemDto));

        doNothing().when(producer).sendTransaction(any(CheckoutRequest.class));

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(producer, times(1)).sendTransaction(any(CheckoutRequest.class));
    }

    @Test
    void shouldReturnTransactionWhenFound() throws Exception {
        Item item = new Item("p1", 2, BigDecimal.TEN);
        Transaction transaction = new Transaction("tx-1", "store-1", "term-1", List.of(item));

        when(repository.findById("tx-1")).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/v1/checkout/tx-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx-1"))
                .andExpect(jsonPath("$.storeId").value("store-1"))
                .andExpect(jsonPath("$.totalAmount").value(20.0));
    }

    @Test
    void shouldReturn404WhenTransactionNotFound() throws Exception {
        when(repository.findById("absent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/checkout/absent"))
                .andExpect(status().isNotFound());
    }
}
