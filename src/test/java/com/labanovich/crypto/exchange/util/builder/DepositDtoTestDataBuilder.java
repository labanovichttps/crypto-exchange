package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.dto.DepositDto;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepositDto")
public class DepositDtoTestDataBuilder implements TestDataBuilder<DepositDto> {

    private BigDecimal amount = BigDecimal.TEN;
    private CurrencyType currencyType = CurrencyType.EUR;
    private String walletAddressTo = String.valueOf(UUID.randomUUID());

    @Override
    public DepositDto build() {
        return DepositDto.builder()
            .amount(amount)
            .currencyType(currencyType)
            .walletAddressTo(walletAddressTo)
            .build();
    }
}
