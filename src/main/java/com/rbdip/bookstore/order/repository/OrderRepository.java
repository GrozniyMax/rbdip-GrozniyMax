package com.rbdip.bookstore.order.repository;

import com.rbdip.bookstore.order.repository.entities.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // @EntityGraph нужен, т.к. open-in-view=false: чтение заказов происходит вне
    // транзакции, поэтому клиент должен быть извлечён тем же запросом.
    @EntityGraph(attributePaths = "customer")
    @Query("select o from Order o")
    List<Order> findAllWithCustomer();
}