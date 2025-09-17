package com.example.ecommerce.app;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.product.Product;
import com.example.ecommerce.product.ProductRepository;

@RestController
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }
    
    @GetMapping("/products")
    public List<Product> getProducts() {
        return repo.findAll();
    }
}
