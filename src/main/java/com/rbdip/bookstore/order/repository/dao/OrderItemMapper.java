package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import com.rbdip.bookstore.product.domain.Product;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменным {@link OrderItem} и JPA-сущностью
 * {@code entities.OrderItem}. Сущность денормализована: хранит название и цену
 * товара напрямую вместо ссылки на products, поэтому id/описание товара при
 * чтении недоступны (нормализация - цель ЛР2).
 */
@Component
public class OrderItemMapper implements Mapper<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem> {

    @Override
    public com.rbdip.bookstore.order.repository.entities.OrderItem map(OrderItem domain) {
        return new com.rbdip.bookstore.order.repository.entities.OrderItem(
                domain.orderId(),
                domain.product().name(),
                domain.product().price(),
                domain.quantity());
    }

    @Override
    public OrderItem unmap(com.rbdip.bookstore.order.repository.entities.OrderItem entity) {
        return new OrderItem(
                entity.getId(),
                entity.getOrderId(),
                new Product(null, entity.getProductName(), entity.getProductPrice(), null),
                entity.getQuantity());
    }
}