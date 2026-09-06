package com.rbdip.bookstore.order.service.domain;

import com.rbdip.bookstore.order.service.domain.discount.AmountDiscount;
import com.rbdip.bookstore.order.service.domain.discount.Discount;
import com.rbdip.bookstore.order.service.domain.discount.PercentDiscount;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Коды купонов и применяемые ими скидки (значения из {@code PricingCalculator}):
 * SAVE10 - фиксированная сумма, SAVE20PERCENT - процент от суммы.
 */
public enum CouponCodes {

    SAVE10(new AmountDiscount(new BigDecimal("10"))),
    SAVE20PERCENT(new PercentDiscount(new BigDecimal("0.8")));

    private final Discount discount;

    CouponCodes(Discount discount) {
        this.discount = discount;
    }

    public Discount getDiscount() {
        return discount;
    }

    public static Optional<CouponCodes> withCode(String code) {
        for (CouponCodes value : values()) {
            if (value.name().equals(code)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}