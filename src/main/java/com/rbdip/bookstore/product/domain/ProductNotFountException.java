package com.rbdip.bookstore.product.domain;

import com.rbdip.bookstore.abstracts.ItemNotFountException;

public class ProductNotFountException extends ItemNotFountException {
    public ProductNotFountException(Long productId) {
        super("product", productId);
    }
}
