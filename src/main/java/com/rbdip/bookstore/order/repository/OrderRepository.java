package com.rbdip.bookstore.order.repository;

import com.rbdip.bookstore.order.repository.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
