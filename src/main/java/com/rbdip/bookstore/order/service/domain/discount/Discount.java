package com.rbdip.bookstore.order.service.domain.discount;

import java.math.BigDecimal;

public interface Discount {

    static Discount Zero() {
        return total -> total;
    }

    BigDecimal apply(BigDecimal total);
}
