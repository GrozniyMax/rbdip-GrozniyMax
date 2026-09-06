package com.rbdip.bookstore.product.repository.dao;

import com.rbdip.bookstore.abstracts.AbstractDao;
import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductDao extends AbstractDao<Product, com.rbdip.bookstore.product.repository.entities.Product, Long> {

    protected ProductDao(
            JpaRepository<com.rbdip.bookstore.product.repository.entities.Product, Long> repository,
            Mapper<Product, com.rbdip.bookstore.product.repository.entities.Product> mapper) {
        super(repository, mapper);
    }
}