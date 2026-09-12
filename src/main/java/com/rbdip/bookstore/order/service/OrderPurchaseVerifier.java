package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.repository.OrderItemRepository;
import com.rbdip.bookstore.review.service.PurchaseVerifier;
import org.springframework.stereotype.Component;

/**
 * Адаптер, реализующий порт review {@link PurchaseVerifier} на стороне модуля
 * order (ЛР4, Strangler Fig). Модуль review зависит только от интерфейса,
 * а не от внутренних репозиториев order.
 */
@Component
public class OrderPurchaseVerifier implements PurchaseVerifier {

    private final OrderItemRepository orderItemRepository;

    public OrderPurchaseVerifier(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public boolean isVerifiedPurchase(Long productId) {
        return orderItemRepository.existsByProductId(productId);
    }
}