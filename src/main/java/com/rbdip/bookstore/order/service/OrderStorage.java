package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.repository.dao.CustomerDao;
import com.rbdip.bookstore.order.repository.dao.OrderDao;
import com.rbdip.bookstore.order.repository.dao.OrderItemDao;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import com.rbdip.bookstore.order.service.domain.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderStorage {

    public record FullOrder(
            Order order,
            List<OrderItem> items
    ){}


    private final OrderDao orderDao;

    private final OrderItemDao orderItemDao;

    private final CustomerDao customerDao;

    public OrderStorage(OrderDao orderDao, OrderItemDao orderItemDao, CustomerDao customerDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.customerDao = customerDao;
    }

    @Transactional
    public Order createOrder(Customer customer, List<OrderItem> items) {
        // Нормализованная схема (ЛР2): сначала персистим клиента, получаем его id,
        // затем создаём заказ, ссылающийся на клиента внешним ключом.
        Long customerId = customerDao.saveAndGetId(customer);
        Order savedOrder = orderDao.create(customerId, OrderStatus.NEW);

        List<OrderItem> persistedItems = items.stream()
                .map(item -> new OrderItem(item.id(), savedOrder.id(), item.product(), item.quantity()))
                .toList();
        orderItemDao.saveAll(persistedItems);

        return savedOrder;
    }

    public List<FullOrder> findAll() {
        List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> {
                    // N+1: отдельный запрос на позиции для каждого заказа вместо
                    // одного JOIN FETCH / batch-запроса. Цель для ЛР4.
                    List<OrderItem> items = orderItemDao.findByOrderId(order.id());
                    return new FullOrder(order, items);
                })
                .toList();
    }
}