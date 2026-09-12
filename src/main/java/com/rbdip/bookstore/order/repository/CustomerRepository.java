package com.rbdip.bookstore.order.repository;

import com.rbdip.bookstore.order.repository.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}