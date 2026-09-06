package com.rbdip.bookstore.order.controller.dto.out;

import java.util.List;

/**
 * DTO ответа {@code GET /orders}: заказ вместе с его позициями.
 * Не зависит от персистентных сущностей.
 */
public record OrderDto(
        Long id,
        String customerFullName,
        String status,
        List<OrderItemDto> items) {
}