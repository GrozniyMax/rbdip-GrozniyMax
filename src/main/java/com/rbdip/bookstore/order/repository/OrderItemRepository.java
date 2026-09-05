package com.rbdip.bookstore.order.repository;

import java.util.List;

import com.rbdip.bookstore.order.repository.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Намеренно нет @EntityGraph/JOIN FETCH-варианта - используется в цикле
    // контроллером, что и создаёт N+1 (см. OrderController#listOrders).
    List<OrderItem> findByOrderId(Long orderId);
}
