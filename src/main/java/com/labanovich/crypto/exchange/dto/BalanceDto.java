package com.labanovich.crypto.exchange.dto;

import com.labanovich.crypto.exchange.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDto {

    private BigDecimal amount;
    private CurrencyType currency;
    private LocalDateTime createdDatetime;
    private LocalDateTime updateDatetime;
}
