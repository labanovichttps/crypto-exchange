package com.labanovich.crypto.exchange.service;

import com.labanovich.crypto.exchange.dto.OrderCreateDto;
import com.labanovich.crypto.exchange.entity.Order;
import com.labanovich.crypto.exchange.enums.OrderStatus;
import com.labanovich.crypto.exchange.enums.OrderType;
import com.labanovich.crypto.exchange.mapper.OrderMapper;
import com.labanovich.crypto.exchange.repository.OrderRepository;
import com.labanovich.crypto.exchange.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WalletService walletService;

    @Transactional
    public void matchAllOrders() {
        var pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (var order : pendingOrders) {
            matchOrders(order);
        }
    }

    public void create(OrderCreateDto orderCreateDto) {
        var user = userService.get(SecurityUtil.getCurrentUsername());
        var orderForSave = orderMapper.map(orderCreateDto, user);
        orderRepository.save(orderForSave);
    }

    public void matchOrders(Order newOrder) {
        var ordersToMatch = orderRepository.findByCurrencyAndTypeAndStatus(
            newOrder.getCurrency(),
            newOrder.getType() == OrderType.BUY ? OrderType.SELL : OrderType.BUY,
            OrderStatus.PENDING
        );

        var remainingAmount = newOrder.getAmount();

        for (var order : ordersToMatch) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) break;

            var matchAmount = order.getAmount().min(remainingAmount);
            remainingAmount = remainingAmount.subtract(matchAmount);
            order.setAmount(order.getAmount().subtract(matchAmount));

            if (order.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                order.complete();
            } else {
                order.partiallyFailed();
            }

            if (newOrder.getType() == OrderType.BUY) {
                walletService.depositForOrder(order.getUser(), order.getCurrency(), matchAmount);
                walletService.withdrawForOrder(newOrder.getUser(), order.getCurrency(), matchAmount);
            } else {
                walletService.withdrawForOrder(order.getUser(), order.getCurrency(), matchAmount);
                walletService.depositForOrder(newOrder.getUser(), order.getCurrency(), matchAmount);
            }

            orderRepository.save(order);
        }

        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            newOrder.setStatus(OrderStatus.COMPLETED);
        } else {
            newOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
            newOrder.setAmount(remainingAmount);
        }

        orderRepository.save(newOrder);
    }

}
