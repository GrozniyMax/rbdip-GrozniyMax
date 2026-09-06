package com.rbdip.bookstore.order.service;

import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderNotifier {

    public void sendConfirmationEmail(Customer customer, Order order, BigDecimal total) {
        // Реальный почтовый транспорт не настроен в учебном проекте - здесь
        // просто эмулируется побочный эффект отправки письма.
        System.out.printf(
                "[email] Dear %s, your order #%d for %s has been placed.%n", customer.fullName(), order.id(), total);
    }
}
