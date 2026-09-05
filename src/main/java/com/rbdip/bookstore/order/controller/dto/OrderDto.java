package com.rbdip.bookstore.order.controller.dto;

import java.util.List;

/**
 * DTO ответа {@code GET /orders}: заказ вместе с его позициями.
 * Не зависит от персистентных сущностей.
 */
public class OrderDto {

    private final Long id;
    private final String customerFullName;
    private final String status;
    private final List<OrderItemDto> items;

    public OrderDto(Long id, String customerFullName, String status, List<OrderItemDto> items) {
        this.id = id;
        this.customerFullName = customerFullName;
        this.status = status;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }
}