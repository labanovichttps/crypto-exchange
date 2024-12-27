package com.labanovich.crypto.exchange.dto;

import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.enums.OrderKind;
import com.labanovich.crypto.exchange.enums.OrderStatus;
import com.labanovich.crypto.exchange.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {

    private OrderType type;
    private OrderStatus status;
    private OrderKind kind;
    private BigDecimal amount;
    private CurrencyType currency;
}
