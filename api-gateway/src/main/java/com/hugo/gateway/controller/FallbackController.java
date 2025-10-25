package com.hugo.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    
    @GetMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("Serviço indisponível no momento. Por favor, tente novamente mais tarde.");
    }
    
    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("API Gateway está em execução! Serviços disponíveis: /api/products, /api/orders");
    }
}