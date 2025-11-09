package com.ecommerce.app;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.common.TenantContext;
import com.ecommerce.record.Greeting;

@RestController
public class HelloController {
    private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

    
    @GetMapping("/")
    public String home(){
        return "Ace says Hello!!";
    }
    @GetMapping("/hello")
    public String sayHello() {
        String tenant = TenantContext.getTenantId();
        return "Hello from tenant: " + (tenant == null ? "Input X-Tenant-Id" : tenant );
    }
    @GetMapping("/greeting")
	public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
    
}
