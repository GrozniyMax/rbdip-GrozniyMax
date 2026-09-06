package com.rbdip.bookstore.order.service.domain;

import java.util.Optional;

public enum OrderStatus {
    NEW("new");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public static Optional<OrderStatus> withValue(String value) {
        for (OrderStatus status : values()) {
            if (status.value.equals(value)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
