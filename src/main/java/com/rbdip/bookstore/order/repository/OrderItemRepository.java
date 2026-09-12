package com.rbdip.bookstore.order.repository;

import com.rbdip.bookstore.order.repository.entities.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // JOIN FETCH нужен, т.к. open-in-view=false: чтение позиций происходит вне
    // транзакции, поэтому товар должен быть извлечён тем же запросом.
    @Query("select oi from OrderItem oi join fetch oi.product where oi.orderId = :orderId")
    List<OrderItem> findByOrderId(Long orderId);
}