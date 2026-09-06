package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.repository.OrderItemRepository;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDao extends AbstractDao<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem, Long> {

    private final OrderItemRepository orderItemRepository;

    protected OrderItemDao(
            OrderItemRepository repository,
            Mapper<OrderItem, com.rbdip.bookstore.order.repository.entities.OrderItem> mapper) {
        super(repository, mapper);
        this.orderItemRepository = repository;
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(mapper::unmap)
                .toList();
    }
}