package com.rbdip.bookstore.order.service.domain;

import com.rbdip.bookstore.product.service.domain.Product;

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

    public BigDecimal getTotalPrice() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}