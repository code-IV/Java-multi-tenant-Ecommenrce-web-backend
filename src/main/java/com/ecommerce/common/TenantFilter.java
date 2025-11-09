package com.ecommerce.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId != null && !tenantId.trim().isEmpty()) {
            // Basic validation: alphanumeric characters, hyphens, underscores only
            if (tenantId.matches("^[a-zA-Z0-9_-]+$") && tenantId.length() <= 50) {
                TenantContext.setTenantId(tenantId);
            } else {
                // Log invalid tenant ID attempt for security monitoring
                // Could also return 400 Bad Request or use default tenant
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
