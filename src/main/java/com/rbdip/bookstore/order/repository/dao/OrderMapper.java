package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.OrderStatus;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.order.service.domain.customer.CustomerType;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменным {@link Order} и JPA-сущностью {@code entities.Order}.
 * Сущность денормализована и хранит контактные данные клиента напрямую,
 * поэтому при чтении они сворачиваются в {@link Customer}, а при записи
 * разворачиваются обратно.
 */
@Component
public class OrderMapper implements Mapper<Order, com.rbdip.bookstore.order.repository.entities.Order> {

    @Override
    public com.rbdip.bookstore.order.repository.entities.Order map(Order domain) {
        return new com.rbdip.bookstore.order.repository.entities.Order(
                domain.customer().fullName(),
                domain.customer().address(),
                domain.customer().phone(),
                domain.status().getValue());
    }

    @Override
    public Order unmap(com.rbdip.bookstore.order.repository.entities.Order entity) {
        return new Order(
                entity.getId(),
                new Customer(
                        entity.getCustomerFullName(),
                        entity.getCustomerAddress(),
                        entity.getCustomerPhone(),
                        CustomerType.REGULAR),
                OrderStatus.withValue(entity.getStatus()).orElse(OrderStatus.NEW),
                entity.getCreatedAt());
    }
}