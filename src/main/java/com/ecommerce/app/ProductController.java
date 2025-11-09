package com.ecommerce.app;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.validation.BindingResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ecommerce.product.Product;
import com.ecommerce.product.ProductRepository;

@Controller
public class ProductController implements WebMvcConfigurer {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/result").setViewName("result");
    }

    
    @GetMapping("/api/products")
    @ResponseBody
    public List<Product> getProducts() {
        return repo.findAll();
    }

    @GetMapping("/products")
    public String productsPage(Product product) {
        // This should correspond to a Thymeleaf template named form.html
        return "form";
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        Optional<Product> product = repo.findById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PatchMapping("/api/product/{id}")
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

    @PostMapping("/api/product")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product newProduct){        
        Product savedProduct = repo.save(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PostMapping("/products")
    public String createProductPage(@Valid @ModelAttribute Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> System.out.println(error));
            return "form"; // Return to the form if there are validation errors
        }
        repo.save(product);
        return "redirect:/result"; // This should correspond to a Thymeleaf template named createProduct.html
    }

    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
