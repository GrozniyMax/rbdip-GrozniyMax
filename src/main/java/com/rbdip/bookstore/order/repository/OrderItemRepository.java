package com.rbdip.bookstore.order.repository;

import com.rbdip.bookstore.order.repository.entities.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Батч-выборка позиций для набора заказов одним запросом (ЛР4): вместо
    // N+1 отдельного запроса на каждый заказ. JOIN FETCH нужен, т.к.
    // open-in-view=false - чтение происходит вне транзакции.
    @Query("select oi from OrderItem oi join fetch oi.product where oi.orderId in :orderIds")
    List<OrderItem> findAllByOrderIdIn(@Param("orderIds") List<Long> orderIds);

    boolean existsByProductId(Long productId);
}