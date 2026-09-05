package com.rbdip.bookstore.order.controller.dto;

import java.util.List;

public record CreateOrderRequest(
        String customerFullName,
        String customerAddress,
        String customerPhone,
        String customerType,
        String couponCode,
        List<Item> items) {

    public record Item(Long productId, Integer quantity) {
    }
}
