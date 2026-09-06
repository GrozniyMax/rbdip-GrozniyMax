package com.rbdip.bookstore.product.repository.dao;

import com.rbdip.bookstore.abstracts.Mapper;
import com.rbdip.bookstore.product.domain.Product;
import org.springframework.stereotype.Component;

/**
 * Маппер между доменным {@link Product} и JPA-сущностью {@code entities.Product}.
 */
@Component
public class ProductMapper implements Mapper<Product, com.rbdip.bookstore.product.repository.entities.Product> {

    @Override
    public com.rbdip.bookstore.product.repository.entities.Product map(Product domain) {
        return new com.rbdip.bookstore.product.repository.entities.Product(
                domain.name(),
                domain.price(),
                domain.description());
    }

    @Override
    public Product unmap(com.rbdip.bookstore.product.repository.entities.Product entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getDescription());
    }
}