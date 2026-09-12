package com.rbdip.bookstore.order.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.order.repository.CustomerRepository;
import com.rbdip.bookstore.order.service.domain.customer.Customer;
import org.springframework.stereotype.Component;

/**
 * DAO клиентов. Доменная модель {@link Customer} не содержит id (он живёт
 * только в персистентной сущности), поэтому для получения идентификатора
 * при создании заказа используется {@link #saveAndGetId(Customer)}.
 */
@Component
public class CustomerDao extends AbstractDao<Customer, com.rbdip.bookstore.order.repository.entities.Customer, Long> {

    protected CustomerDao(
            CustomerRepository repository,
            Mapper<Customer, com.rbdip.bookstore.order.repository.entities.Customer> mapper) {
        super(repository, mapper);
    }

    public Long saveAndGetId(Customer customer) {
        return repository.save(mapper.map(customer)).getId();
    }
}