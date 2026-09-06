package com.rbdip.bookstore.review.controller.dto.out;

/**
 * DTO отзыва в ответе {@code GET /products/{productId}/reviews}.
 */
public record ReviewDto(
        String authorName,
        Integer rating,
        String comment) {
}