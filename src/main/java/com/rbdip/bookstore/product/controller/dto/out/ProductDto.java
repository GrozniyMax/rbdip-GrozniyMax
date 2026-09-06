package com.rbdip.bookstore.product.controller.dto.out;

import java.math.BigDecimal;

/**
 * DTO ответа {@code GET /products}: товар без привязки к персистентной
 * сущности.
 */
public record ProductDto(
        Long id,
        String name,
        BigDecimal price) {
}