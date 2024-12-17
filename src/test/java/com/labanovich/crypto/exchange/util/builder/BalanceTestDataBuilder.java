package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.entity.Balance;
import com.labanovich.crypto.exchange.entity.Wallet;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aBalance")
public class BalanceTestDataBuilder implements TestDataBuilder<Balance> {

    private Long id = 420L;
    private BigDecimal amount = BigDecimal.TEN;
    private CurrencyType currency = CurrencyType.BTC;
    private LocalDateTime createdDatetime = LocalDateTime.of(2004, 4, 18, 10, 20);
    private LocalDateTime updateDatetime = LocalDateTime.of(2004, 4, 18, 10, 20);
    private Wallet wallet = null;

    @Override
    public Balance build() {
        return Balance.builder()
            .id(id)
            .amount(amount)
            .currency(currency)
            .createdDatetime(createdDatetime)
            .updateDatetime(updateDatetime)
            .wallet(wallet)
            .build();
    }
}
