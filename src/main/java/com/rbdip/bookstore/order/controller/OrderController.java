package com.rbdip.bookstore.order.controller;

import com.rbdip.bookstore.order.controller.dto.in.CreateOrderRequest;
import com.rbdip.bookstore.order.controller.dto.out.OrderCreationResult;
import com.rbdip.bookstore.order.controller.dto.out.OrderDto;
import com.rbdip.bookstore.order.service.OrderCreator;
import com.rbdip.bookstore.order.service.OrderStorage;
import com.rbdip.bookstore.order.service.domain.Order;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderStorage orderStorage;
    private final OrderCreator orderCreator;
    private final DtoMapper mapper;

    public OrderController(OrderStorage orderStorage, OrderCreator orderCreator, DtoMapper mapper) {
        this.orderStorage = orderStorage;
        this.orderCreator = orderCreator;
        this.mapper = mapper;
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreationResult createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Customer customer = new Customer(
                request.customerName(),
                request.customerAddress(),
                request.customerPhone(),
                request.customerType());

        var items = request.items().stream()
                .map(i -> new OrderCreator.OrderItemCreationIntent(i.productId(), i.quantity()))
                .toList();

        Order order = orderCreator.createOrder(customer, items, request.couponCode());

        return mapper.toOrderCreationResult(order);
    }

    @GetMapping("/orders")
    public List<OrderDto> listOrders() {
        List<OrderStorage.FullOrder> orders = orderStorage.findAll();

        return orders.stream()
                .map(order -> mapper.toOrderDto(order.order(), order.items()))
                .toList();
    }
}