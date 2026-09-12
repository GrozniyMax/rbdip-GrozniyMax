package com.rbdip.bookstore.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.rbdip.bookstore.order.service.domain.OrderItem;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.product.domain.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Характеризационные тесты {@link PricingCalculator}: фиксируют текущее
 * поведение расчёта цены (включая граничные и отрицательные ветки), которые
 * эталонный {@code PricingCalculatorReferenceTest} намеренно не покрывает.
 * Это задание ЛР2 (гейт {@code lab2_coverage}) и фундамент для PIT-гейта ЛР5.
 */
class PricingCalculatorTest {

    private final PricingCalculator calculator = new PricingCalculator();

    @Test
    void calculatesSimpleRegularOrderWithoutCoupon() {
        BigDecimal total = calculate(item("10.00", 2), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("20.00");
    }

    @Test
    void appliesVipTenPercentDiscount() {
        BigDecimal total = calculate(item("100.00", 1), customer("vip"), null);

        assertThat(total).isEqualByComparingTo("90.00");
    }

    @Test
    void appliesWholesaleFifteenPercentDiscount() {
        BigDecimal total = calculate(item("100.00", 1), customer("wholesale"), null);

        assertThat(total).isEqualByComparingTo("85.00");
    }

    @Test
    void regularCustomerGetsNoDiscount() {
        BigDecimal total = calculate(item("100.00", 1), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("100.00");
    }

    @Test
    void appliesQuantityDiscountWhenQuantityAboveTen() {
        BigDecimal total = calculate(item("10.00", 11), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("104.50");
    }

    @Test
    void noQuantityDiscountAtBoundaryOfTen() {
        BigDecimal total = calculate(item("10.00", 10), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("100.00");
    }

    @Test
    void appliesFixedAmountCouponSave10() {
        BigDecimal total = calculate(item("50.00", 1), customer("regular"), "SAVE10");

        assertThat(total).isEqualByComparingTo("40.00");
    }

    @Test
    void appliesPercentCouponSave20Percent() {
        BigDecimal total = calculate(item("50.00", 1), customer("regular"), "SAVE20PERCENT");

        assertThat(total).isEqualByComparingTo("40.00");
    }

    @Test
    void unknownCouponIsIgnored() {
        BigDecimal total = calculate(item("50.00", 1), customer("regular"), "NOPE");

        assertThat(total).isEqualByComparingTo("50.00");
    }

    @Test
    void nullCouponIsIgnored() {
        BigDecimal total = calculate(item("50.00", 1), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("50.00");
    }

    @Test
    void clampsNegativeTotalToZero() {
        BigDecimal total = calculate(item("5.00", 1), customer("regular"), "SAVE10");

        assertThat(total).isEqualByComparingTo("0.00");
    }

    @Test
    void appliesBigTotalDiscountAboveThousand() {
        BigDecimal total = calculate(item("600.00", 2), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("1176.00");
    }

    @Test
    void noBigTotalDiscountAtBoundaryOfThousand() {
        BigDecimal total = calculate(item("1000.00", 1), customer("regular"), null);

        assertThat(total).isEqualByComparingTo("1000.00");
    }

    @Test
    void appliesCustomerDiscountBeforeCoupon() {
        // Сначала VIP (×0.9): 100 → 90, затем SAVE10 (−10): 90 → 80.
        // Если бы порядок был обратным, получилось бы 81.
        BigDecimal total = calculate(item("100.00", 1), customer("vip"), "SAVE10");

        assertThat(total).isEqualByComparingTo("80.00");
    }

    @Test
    void roundsResultToTwoDecimalsHalfUp() {
        // 1.11 × 3 = 3.33; SAVE20PERCENT ×0.8 = 2.664 → 2.66 (HALF_UP).
        BigDecimal total = calculate(item("1.11", 3), customer("regular"), "SAVE20PERCENT");

        assertThat(total).isEqualByComparingTo("2.66");
    }

    @Test
    void combinesQuantityCustomerCouponAndBigTotalDiscounts() {
        // 100 × 20 = 2000; ×0.95 (qty) = 1900; ×0.9 (VIP) = 1710; −10 (SAVE10) = 1700;
        // >1000 → ×0.98 = 1666.00.
        BigDecimal total = calculate(item("100.00", 20), customer("vip"), "SAVE10");

        assertThat(total).isEqualByComparingTo("1666.00");
    }

    private BigDecimal calculate(OrderItem item, Customer customer, String couponCode) {
        return calculator.calculateOrderTotal(List.of(item), customer, couponCode);
    }

    private static OrderItem item(String price, int quantity) {
        return new OrderItem(
                null,
                null,
                new Product(null, "Товар", new BigDecimal(price), null),
                quantity);
    }

    private static Customer customer(String type) {
        return new Customer("Иван Петров", "Москва", "+79990000000", type);
    }
}