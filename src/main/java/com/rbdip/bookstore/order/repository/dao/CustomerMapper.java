package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.order.service.domain.customer.CustomerType;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменным {@link Customer} и JPA-сущностью
 * {@code entities.Customer}. Слой DAO трансформирует данные при
 * персистентности, доменная модель клиента при этом не меняется.
 */
@Component
public class CustomerMapper implements Mapper<Customer, com.rbdip.bookstore.order.repository.entities.Customer> {

    @Override
    public com.rbdip.bookstore.order.repository.entities.Customer map(Customer domain) {
        return new com.rbdip.bookstore.order.repository.entities.Customer(
                domain.fullName(),
                domain.address(),
                domain.phone(),
                domain.type().getCode());
    }

    @Override
    public Customer unmap(com.rbdip.bookstore.order.repository.entities.Customer entity) {
        return new Customer(
                entity.getFullName(),
                entity.getAddress(),
                entity.getPhone(),
                CustomerType.withCode(entity.getType()).orElse(CustomerType.REGULAR));
    }
}