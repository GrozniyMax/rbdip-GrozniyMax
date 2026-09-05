package com.rbdip.bookstore.order.service.domain;

import com.rbdip.bookstore.product.Product;

import java.math.BigDecimal;

/**
 * Доменное представление позиции заказа (business model), не связанное с JPA.
 * Копирует поля сущности {@code OrderItem}.
 */
public record OrderItem(
        Long id,
        Long orderId,
        Product product,
        Integer quantity) {
}