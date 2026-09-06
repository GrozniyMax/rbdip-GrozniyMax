package com.rbdip.bookstore.review.service.domain;

import java.time.Instant;

/**
 * Доменное представление отзыва (business model), не связанное с JPA.
 * Копирует поля сущности {@code Review}.
 */
public record Review(
        Long id,
        Long productId,
        String authorName,
        Integer rating,
        String comment,
        Instant createdAt) {
}