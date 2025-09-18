package com.example.ecommerce.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ProductLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public ProductLoader(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Product product = new Product();
        // product.setName("shoe");
        // product.setPrice(1200.00);
        // productRepository.save(product);
        
        System.out.println("list of saved products");
        productRepository.findAll().forEach(p -> {
            System.out.println(p.getName() + " " + p.getPrice());
        });
    }
    
}
