package com.labanovich.crypto.exchange.controller;

import com.labanovich.crypto.exchange.dto.OrderCreateDto;
import com.labanovich.crypto.exchange.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> placeOrder(@RequestBody OrderCreateDto orderCreateDto) {
        orderService.create(orderCreateDto);
        return ResponseEntity.ok().build();
    }
}
