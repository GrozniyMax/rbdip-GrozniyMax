package com.rbdip.bookstore.order.controller.dto;

/**
 * DTO позиции заказа в ответе {@code GET /orders}.
 */
public class OrderItemDto {

    private final String productName;
    private final Integer quantity;

    public OrderItemDto(String productName, Integer quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }
}