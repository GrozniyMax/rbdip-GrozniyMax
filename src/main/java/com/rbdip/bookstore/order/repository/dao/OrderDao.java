package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.service.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderDao extends AbstractDao<Order, com.rbdip.bookstore.order.repository.entities.Order, Long> {
    protected OrderDao(JpaRepository<com.rbdip.bookstore.order.repository.entities.Order, Long> repository, Mapper<Order, com.rbdip.bookstore.order.repository.entities.Order> mapper) {
        super(repository, mapper);
    }
}
