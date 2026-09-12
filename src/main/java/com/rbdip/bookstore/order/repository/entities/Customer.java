package com.rbdip.bookstore.order.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA-сущность клиента - результат нормализации схемы (ЛР2): контактные
 * данные вынесены из orders в отдельную таблицу customers, на которую
 * заказы ссылаются внешним ключом customer_id.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String address;

    private String phone;

    @Column(nullable = false)
    private String type;

    protected Customer() {
        // for JPA
    }

    public Customer(String fullName, String address, String phone, String type) {
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }
}