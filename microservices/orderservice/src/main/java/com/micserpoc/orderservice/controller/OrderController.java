package com.micserpoc.orderservice.controller;

import com.micserpoc.orderservice.dto.OrderRequestDTO;
import com.micserpoc.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController
{
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")    // set the name attribute from properties
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequestDTO orderRequest)
    {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequestDTO orderRequestDTO, RuntimeException runtimeException)
    {
        return CompletableFuture.supplyAsync(() -> "Oops! Sometime went wrong, please order after some time!");
    }
}
