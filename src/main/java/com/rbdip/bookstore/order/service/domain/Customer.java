package com.rbdip.bookstore.order.service.domain;

public record Customer(
        String fullName,
        String address,
        String phone
) {
}
