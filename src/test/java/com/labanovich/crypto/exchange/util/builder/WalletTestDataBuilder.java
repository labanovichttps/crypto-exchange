package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.entity.Balance;
import com.labanovich.crypto.exchange.entity.User;
import com.labanovich.crypto.exchange.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aWallet")
public class WalletTestDataBuilder implements TestDataBuilder<Wallet> {

    private Long id = 420L;
    private String address = String.valueOf(UUID.randomUUID());
    private User user = UserTestDataBuilder.aUser().build();
    private LocalDateTime createdDatetime = LocalDateTime.of(2004, 4, 18, 10, 20);
    private List<Balance> balances = List.of(BalanceTestDataBuilder.aBalance().build());

    @Override
    public Wallet build() {
        return Wallet.builder()
            .id(id)
            .address(address)
            .user(user)
            .createdDatetime(createdDatetime)
            .balances(balances)
            .build();
    }
}
