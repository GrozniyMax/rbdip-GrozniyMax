package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDao extends AbstractDao<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem, Long> {

    protected OrderItemDao(
            JpaRepository<com.rbdip.bookstore.order.repository.entities.OrderItem, Long> repository,
            Mapper<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem> mapper) {
        super(repository, mapper);
    }
}