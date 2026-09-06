package com.rbdip.bookstore.product.controller;

import com.rbdip.bookstore.product.controller.dto.in.CreateProductRequest;
import com.rbdip.bookstore.product.controller.dto.out.ProductDto;
import com.rbdip.bookstore.product.repository.dao.ProductDao;
import com.rbdip.bookstore.product.domain.Product;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody CreateProductRequest request) {
        Product saved = productDao.save(new Product(null, request.name(), request.price(), request.description()));
        return new ProductDto(saved.id(), saved.name(), saved.price());
    }

    @GetMapping("/products")
    public List<ProductDto> listProducts() {
        return productDao.findAll().stream()
                .map(p -> new ProductDto(p.id(), p.name(), p.price()))
                .toList();
    }
}