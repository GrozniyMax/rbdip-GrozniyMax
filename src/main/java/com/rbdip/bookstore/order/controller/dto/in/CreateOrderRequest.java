package com.rbdip.bookstore.order.controller.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Плоская форма запроса на создание заказа - публичный API фиксирован и
 * должен оставаться совместимым с reference-тестами.
 */
public record CreateOrderRequest(
        @NotBlank
        String customerName,

        @NotBlank
        String customerAddress,

        String customerPhone,

        String customerType,

        String couponCode,

        @NotNull
        @NotEmpty
        @Valid
        List<Item> items) {

    public record Item(
            @NotNull
            Long productId,

            @Positive
            @JsonProperty(value = "quantity", defaultValue = "1")
            Integer quantity) {
    }
}