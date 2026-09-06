package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.service.domain.CouponCodes;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.order.service.domain.discount.Discount;
import com.rbdip.bookstore.order.service.domain.discount.PercentDiscount;
import com.rbdip.bookstore.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Модуль расчёта цены заказа. Намеренно почти не покрыт тестами и
 * содержит magic numbers / нечитаемые ветвления скидок - цель для
 * характеризационных тестов (ЛР2) и mutation-testing гейта PIT (ЛР5).
 */
@Service
public class PricingCalculator {

    // Совместимость со старым (фиксированным) публичным API, который ждёт reference-тест.
    @Deprecated
    public record LineItem(BigDecimal price, int quantity) {
    }

    /**
     * Костыль под reference-тест: конвертирует вход и делегирует в метод с новой сигнатурой.
     * @see PricingCalculator#calculateOrderTotal(List, Customer, String)
     * @param items
     * @param customerType
     * @param couponCode
     * @return
     */
    @Deprecated
    public BigDecimal calculateOrderTotal(List<LineItem> items, String customerType, String couponCode) {
        List<OrderItem> orderItems = items.stream()
                .map(li -> new OrderItem(null, null, new Product(null, null, li.price(), null), li.quantity()))
                .toList();
        return calculateOrderTotal(orderItems, new Customer(null, null, null, customerType), couponCode);
    }

    private static final Discount QUANTITY_DISCOUNT = new PercentDiscount(new BigDecimal("0.95"));

    private static final Discount BIG_TOTAL_VALUE_DISCOUNT = new PercentDiscount(new BigDecimal("0.98"));

    public BigDecimal calculateOrderTotal(List<OrderItem> items, Customer customer, String couponCode) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            var linePrice = item.getTotalPrice();
            total = total.add(applyQuantityDiscount(linePrice, item.quantity()));
        }

        total = applyCustomerDiscount(total, customer);

        total = applyCoupon(total, couponCode);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        total = applyBigTotalValueDiscount(total);

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyCustomerDiscount(BigDecimal value, Customer customer) {
        return customer.getCustomerDiscount().apply(value);
    }

    private BigDecimal applyCoupon(BigDecimal value, String couponCode) {
        return CouponCodes.withCode(couponCode)
                .map(CouponCodes::getDiscount)
                .map(discount -> discount.apply(value))
                .orElse(value);
    }

    private BigDecimal applyQuantityDiscount(BigDecimal value, int quantity) {
        if (quantity > 10) {
            return QUANTITY_DISCOUNT.apply(value);
        }
        return value;
    }

    private BigDecimal applyBigTotalValueDiscount(BigDecimal value) {
        if (value.compareTo(BigDecimal.valueOf(1000)) > 0) {
            return BIG_TOTAL_VALUE_DISCOUNT.apply(value);
        }
        return value;
    }

}
