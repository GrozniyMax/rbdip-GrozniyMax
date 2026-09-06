package com.rbdip.bookstore.order.service.domain.discount;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Скидка на фиксированную сумму: из итога вычитается {@code amount}.
 */
public class AmountDiscount implements Discount {

    private final BigDecimal amount;

    public AmountDiscount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public BigDecimal apply(BigDecimal total) {
        return total.subtract(amount);
    }
}