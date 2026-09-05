package com.rbdip.bookstore.order.service.domain;

import java.time.Instant;

/**
 * Доменное представление заказа (business model), не связанное с JPA.
 * Копирует поля сущности {@code Order}, чтобы сервисный слой мог работать
 * с чистыми данными без персистентной сущности.
 */
public record Order(
        Long id,
        Customer customer,
        String status,
        Instant createdAt) {
}