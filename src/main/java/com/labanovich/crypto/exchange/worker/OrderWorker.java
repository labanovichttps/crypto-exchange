package com.labanovich.crypto.exchange.worker;

import com.labanovich.crypto.exchange.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderWorker {

    private final OrderService orderService;

    @Scheduled(fixedRate = 60000)
    public void scheduleOrderMatching() {
        orderService.matchAllOrders();
    }
}