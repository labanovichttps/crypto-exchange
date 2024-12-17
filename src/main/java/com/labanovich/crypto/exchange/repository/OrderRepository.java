package com.labanovich.crypto.exchange.repository;

import com.labanovich.crypto.exchange.entity.Order;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.enums.OrderStatus;
import com.labanovich.crypto.exchange.enums.OrderType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "user")
    List<Order> findByCurrencyAndTypeAndStatus(CurrencyType currency, OrderType orderType, OrderStatus orderStatus);

    List<Order> findByStatus(OrderStatus orderStatus);
}
