package com.rbdip.bookstore.order.service.domain.discount;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Процентная скидка: итог умножается на множитель (0.9 = скидка 10%).
 */
public class PercentDiscount implements Discount {

    private final BigDecimal multiplier;

    public PercentDiscount(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public BigDecimal apply(BigDecimal total) {
        return total.multiply(multiplier);
    }
}