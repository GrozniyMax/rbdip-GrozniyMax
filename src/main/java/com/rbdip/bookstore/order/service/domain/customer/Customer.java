package com.rbdip.bookstore.order.service.domain.customer;

import com.rbdip.bookstore.order.service.domain.discount.Discount;
import com.rbdip.bookstore.order.service.domain.discount.PercentDiscount;
import java.math.BigDecimal;

public record Customer(
        String fullName,
        String address,
        String phone,
        CustomerType type
) {

    public Customer(String fullName, String address, String phone, String type) {
        this(fullName, address, phone, CustomerType.withCode(type).orElse(CustomerType.REGULAR));
    }

    public Discount getCustomerDiscount() {
        return getForCustomerType(type);
    }

    private Discount getForCustomerType(CustomerType type) {
        return switch (type) {
            case VIP -> new PercentDiscount(new BigDecimal("0.9"));
            case REGULAR -> Discount.Zero();
            case WHOLESALE -> new PercentDiscount(new BigDecimal("0.85"));
        };
    }
}
