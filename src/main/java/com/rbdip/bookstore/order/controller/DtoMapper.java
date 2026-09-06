package com.rbdip.bookstore.order.controller;

import com.rbdip.bookstore.order.controller.dto.out.OrderCreationResult;
import com.rbdip.bookstore.order.controller.dto.out.OrderDto;
import com.rbdip.bookstore.order.controller.dto.out.OrderItemDto;
import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.OrderItem;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public OrderItemDto toOrderDto(OrderItem item) {
        return new OrderItemDto(
                item.product().name(),
                item.quantity());
    }

    public OrderCreationResult toOrderCreationResult(Order order) {
        return new OrderCreationResult(order.id(), order.status().getValue());
    }

    public OrderDto toOrderDto(Order order, List<OrderItem> items) {
        List<OrderItemDto> itemDtos = items.stream()
                .map(this::toOrderDto)
                .toList();
        return new OrderDto(
                order.id(),
                order.customer().fullName(),
                order.status().getValue(),
                itemDtos);
    }


}