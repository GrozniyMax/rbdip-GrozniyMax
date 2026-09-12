package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import com.rbdip.bookstore.order.service.domain.customer.CustomerType;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменным {@link Customer} и JPA-сущностью
 * {@code entities.Customer}. Доменная модель клиента держит имя одной строкой
 * (fullName), а в БД после expand-contract (ЛР3) имя хранится по частям
 * first_name/last_name, поэтому трансформация (split/join) происходит здесь.
 */
@Component
public class CustomerMapper implements Mapper<Customer, com.rbdip.bookstore.order.repository.entities.Customer> {

    @Override
    public com.rbdip.bookstore.order.repository.entities.Customer map(Customer domain) {
        String[] parts = domain.fullName().split(" ", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";
        return new com.rbdip.bookstore.order.repository.entities.Customer(
                firstName,
                lastName,
                domain.address(),
                domain.phone(),
                domain.type().getCode());
    }

    @Override
    public Customer unmap(com.rbdip.bookstore.order.repository.entities.Customer entity) {
        String fullName = (entity.getLastName() == null || entity.getLastName().isEmpty())
                ? entity.getFirstName()
                : entity.getFirstName() + " " + entity.getLastName();
        return new Customer(
                fullName,
                entity.getAddress(),
                entity.getPhone(),
                CustomerType.withCode(entity.getType()).orElse(CustomerType.REGULAR));
    }
}