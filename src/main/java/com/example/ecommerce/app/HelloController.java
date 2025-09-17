package com.example.ecommerce.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.common.TenantContext;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String home(){
        return "Ace says Hello!!";
    }
    @GetMapping("/hello")
    public String sayHello() {
        String tenant = TenantContext.getTenantId();
        return "Hello from tenant: " + (tenant == null ? "Input X-Tenant-Id" : tenant );
    }
    
    
}
