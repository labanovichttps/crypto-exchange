package com.labanovich.crypto.exchange.dto;


import com.labanovich.crypto.exchange.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalDto {

    private BigDecimal amount;
    private CurrencyType currencyType;
    private String walletAddressFrom;
    private String walletAddressTo;
}
