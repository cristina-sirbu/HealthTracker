package com.healthtracker.healthtracker.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FreshnessRoutingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(FreshnessRoutingFilter.class);
    private static final String HDR = "X-Read-Primary-Until";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HDR);
        if (FreshnessToken.isValid(token)) {
            // In a real app you would set a routing hint in a context here
            log.info("Freshness token valid -> route READs to PRIMARY for this request");
        }
        filterChain.doFilter(request, response);
    }
}
