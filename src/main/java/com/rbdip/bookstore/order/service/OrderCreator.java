package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import com.rbdip.bookstore.product.repository.dao.ProductDao;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.rbdip.bookstore.product.domain.Product;
import com.rbdip.bookstore.product.domain.ProductNotFountException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * God-класс: валидация, расчёт цены, персистентность и "уведомление
 * клиента" смешаны в одном методе. Цель для рефакторинга по SRP в ЛР1.
 */
@Service
public class OrderCreator {

    public record OrderItemCreationIntent(
            Long productId,
            Integer quantity
    ) {}

    private final OrderStorage orderStorage;
    private final ProductDao productDao;
    private final PricingCalculator pricingCalculator;
    private final OrderNotifier orderNotifier;

    public OrderCreator(OrderStorage orderStorage, ProductDao productDao, PricingCalculator pricingCalculator, OrderNotifier orderNotifier) {
        this.orderStorage = orderStorage;
        this.productDao = productDao;
        this.pricingCalculator = pricingCalculator;
        this.orderNotifier = orderNotifier;
    }


    @Transactional
    public Order createOrder(
            Customer customer,
            List<OrderItemCreationIntent> items,
            String couponCode) {

        var productIds = items.stream()
                .map(OrderItemCreationIntent::productId)
                .toList();

        var products = productDao.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::id, product -> product));

        var orderItem = items.stream()
                .map(item -> {

                    var product = products.get(item.productId());
                    if (product == null) {
                        throw new ProductNotFountException(item.productId());
                    }
                    return new OrderItem(
                            null,
                            null,
                            product,
                            item.quantity()
                            );
                        }
                ).toList();


        BigDecimal total = pricingCalculator.calculateOrderTotal(
                orderItem, customer, couponCode);

        var order = orderStorage.createOrder(customer, orderItem);

        orderNotifier.sendConfirmationEmail(customer, order, total);

        return order;
    }
}
