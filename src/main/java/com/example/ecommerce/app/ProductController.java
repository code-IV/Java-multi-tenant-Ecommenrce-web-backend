package com.example.ecommerce.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        Optional<Product> product = repo.findById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PatchMapping("/product/{id}")
    public ResponseEntity<Product>updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct){
        Optional<Product> productOptional = repo.findById(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            if(updatedProduct.getName() != null){
                product.setName(updatedProduct.getName());
            }
            if(updatedProduct.getPrice() != null){
                product.setPrice(updatedProduct.getPrice());
            }
            Product savedProduct = repo.save(product);
            return ResponseEntity.ok(savedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@RequestBody Product newProduct){
        // Add basic validation
        if(newProduct.getName() == null || newProduct.getName().trim().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if(newProduct.getPrice() == null || new BigDecimal(newProduct.getPrice().toString()).compareTo(BigDecimal.ZERO) < 0){
            return ResponseEntity.badRequest().build();
        }
        
        Product savedProduct = repo.save(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
