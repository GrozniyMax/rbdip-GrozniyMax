package com.rbdip.bookstore.product.domain;

import java.math.BigDecimal;

/**
 * Доменное представление товара (business model), не связанное с JPA.
 * Копирует поля сущности {@code Product}.
 */
public record Product(
        Long id,
        String name,
        BigDecimal price,
        String description) {
}