package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.entity.Order;
import com.labanovich.crypto.exchange.entity.User;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.enums.OrderKind;
import com.labanovich.crypto.exchange.enums.OrderStatus;
import com.labanovich.crypto.exchange.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrder")
public class OrderTestDataBuilder implements TestDataBuilder<Order> {

    private Long id = 420L;
    private OrderType type = OrderType.BUY;
    private OrderStatus status = OrderStatus.COMPLETED;
    private OrderKind kind = OrderKind.LIMIT;
    private BigDecimal amount = BigDecimal.TEN;
    private CurrencyType currency = CurrencyType.EUR;
    private User user = UserTestDataBuilder.aUser().build();
    private LocalDateTime createdDatetime = LocalDateTime.of(2004, 4, 18, 10, 20);

    @Override
    public Order build() {
        return Order.builder()
            .id(id)
            .type(type)
            .status(status)
            .kind(kind)
            .amount(amount)
            .currency(currency)
            .user(user)
            .createdDatetime(createdDatetime)
            .build();
    }
}
