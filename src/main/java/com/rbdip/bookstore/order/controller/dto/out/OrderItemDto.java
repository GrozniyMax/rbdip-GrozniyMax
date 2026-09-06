package com.rbdip.bookstore.order.controller.dto.out;

/**
 * DTO позиции заказа в ответе {@code GET /orders}.
 */
public record OrderItemDto(
        String productName,
        Integer quantity) {
}