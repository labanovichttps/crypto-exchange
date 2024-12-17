package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.dto.WithdrawalDto;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aWithdrawalDto")
public class WithdrawalDtoTestDataBuilder implements TestDataBuilder<WithdrawalDto> {

    private BigDecimal amount = BigDecimal.TEN;
    private CurrencyType currencyType = CurrencyType.EUR;
    private String walletAddressTo = String.valueOf(UUID.randomUUID());
    private String walletAddressFrom = String.valueOf(UUID.randomUUID());

    @Override
    public WithdrawalDto build() {
        return WithdrawalDto.builder()
            .amount(amount)
            .currencyType(currencyType)
            .walletAddressTo(walletAddressTo)
            .walletAddressFrom(walletAddressFrom)
            .build();
    }
}
