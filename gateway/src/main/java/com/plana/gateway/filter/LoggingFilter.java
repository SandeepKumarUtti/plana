package com.plana.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        String queryParams = exchange.getRequest().getURI().getQuery();

        log.info("REQUEST -> {} {}{}", method, path, queryParams != null ? "?" + queryParams : "");

        return chain.filter(exchange)
            .then(Mono.fromRunnable(() -> {

                int status = exchange.getResponse().getStatusCode().value();
                log.info("RESPONSE -> {} {}", path, status);

            }));
    }

    @Override
    public int getOrder() {
        return -1; // Run before all filters
    }
}

