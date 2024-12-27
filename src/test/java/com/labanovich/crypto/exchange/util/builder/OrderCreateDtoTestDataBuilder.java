package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.dto.OrderCreateDto;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.enums.OrderKind;
import com.labanovich.crypto.exchange.enums.OrderStatus;
import com.labanovich.crypto.exchange.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrderCreateDto")
public class OrderCreateDtoTestDataBuilder implements TestDataBuilder<OrderCreateDto> {

    private OrderType type = OrderType.BUY;
    private OrderStatus status = OrderStatus.COMPLETED;
    private OrderKind kind = OrderKind.LIMIT;
    private BigDecimal amount = BigDecimal.TEN;
    private CurrencyType currency = CurrencyType.EUR;

    @Override
    public OrderCreateDto build() {
        return OrderCreateDto.builder()
            .type(type)
            .status(status)
            .kind(kind)
            .amount(amount)
            .currency(currency)
            .build();
    }
}