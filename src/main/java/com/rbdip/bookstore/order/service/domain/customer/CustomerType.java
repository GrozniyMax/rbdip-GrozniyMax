package com.rbdip.bookstore.order.service.domain.customer;

import java.util.Optional;

public enum CustomerType {

    VIP("vip"),
    REGULAR("regular"),
    WHOLESALE("wholesale");

    private final String code;

    CustomerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Optional<CustomerType> withCode(String code) {
        for (CustomerType customerType : values()) {
            if (customerType.code.equals(code)) {
                return Optional.of(customerType);
            }
        }
        return Optional.empty();
    }
}
