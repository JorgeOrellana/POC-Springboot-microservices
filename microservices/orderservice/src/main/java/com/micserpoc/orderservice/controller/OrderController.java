package com.micserpoc.orderservice.controller;

import com.micserpoc.orderservice.dto.OrderRequestDTO;
import com.micserpoc.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController
{
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequestDTO orderRequest)
    {
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }

    public String fallbackMethod(OrderRequestDTO orderRequestDTO, RuntimeException runtimeException)
    {
        return "Oops! Sometime went wrong, please order after some time!";
    }
}
