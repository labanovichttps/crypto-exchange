package com.labanovich.crypto.exchange.service;

import com.labanovich.crypto.exchange.dto.WalletDto;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.mapper.WalletMapper;
import com.labanovich.crypto.exchange.repository.WalletRepository;
import com.labanovich.crypto.exchange.util.builder.BalanceTestDataBuilder;
import com.labanovich.crypto.exchange.util.builder.DepositDtoTestDataBuilder;
import com.labanovich.crypto.exchange.util.builder.UserTestDataBuilder;
import com.labanovich.crypto.exchange.util.builder.WalletTestDataBuilder;
import com.labanovich.crypto.exchange.util.builder.WithdrawalDtoTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;
    @Mock
    private UserService userService;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;
    private static final String TEST_USERNAME = "labanovich";

    void initSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication())
            .thenReturn(authentication);
        when(authentication.getPrincipal())
            .thenReturn(userDetails);
        when(userDetails.getUsername())
            .thenReturn(TEST_USERNAME);
    }

    @Test
    void shouldCreateWallet() {
        initSecurityContext();
        var user = UserTestDataBuilder.aUser()
            .withUsername(TEST_USERNAME)
            .withWallet(null)
            .build();
        doReturn(user)
            .when(userService).get(any());

        walletService.create();

        verify(walletRepository).save(any());
    }

    @Test
    void shouldGetWallets() {
        initSecurityContext();
        var wallets = List.of(WalletTestDataBuilder.aWallet().build());
        var walletDto = new WalletDto();
        doReturn(wallets)
            .when(walletRepository).findAllByUserUsername(any());
        doReturn(walletDto)
            .when(walletMapper).map(any());

        var result = walletService.getWallets();

        verify(walletRepository)
            .findAllByUserUsername(TEST_USERNAME);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(walletDto);
    }

    @Test
    void shouldDeposit() {
        initSecurityContext();
        var user = UserTestDataBuilder.aUser()
            .withUsername(TEST_USERNAME)
            .build();
        var balance = BalanceTestDataBuilder.aBalance()
            .withAmount(BigDecimal.ZERO)
            .withCurrency(CurrencyType.EUR)
            .build();
        var wallet = WalletTestDataBuilder.aWallet()
            .withUser(user)
            .withBalances(List.of(balance))
            .build();
        var depositDto = DepositDtoTestDataBuilder.aDepositDto()
            .withWalletAddressTo(wallet.getAddress())
            .withCurrencyType(balance.getCurrency())
            .withAmount(BigDecimal.valueOf(100))
            .build();
        doReturn(user)
            .when(userService).get(any());
        doReturn(Optional.of(wallet))
            .when(walletRepository).findByAddressAndUserUsername(wallet.getAddress(), user.getUsername());
        walletService.deposit(depositDto);
        verify(walletRepository).findByAddressAndUserUsername(wallet.getAddress(), user.getUsername());
        assertThat(balance.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldWithdraw() {
        initSecurityContext();
        var balanceTo = BalanceTestDataBuilder.aBalance()
            .withCurrency(CurrencyType.BTC)
            .withAmount(BigDecimal.ZERO)
            .build();
        var balanceFrom = BalanceTestDataBuilder.aBalance()
            .withCurrency(CurrencyType.BTC)
            .withAmount(BigDecimal.TEN)
            .build();
        var user = UserTestDataBuilder.aUser()
            .withUsername(TEST_USERNAME)
            .build();
        var walletFrom = WalletTestDataBuilder.aWallet()
            .withUser(user)
            .withBalances(List.of(balanceFrom))
            .build();
        var walletTo = WalletTestDataBuilder.aWallet()
            .withBalances(List.of(balanceTo))
            .build();
        balanceFrom.deposit(BigDecimal.valueOf(10));
        var withdrawalDto = WithdrawalDtoTestDataBuilder.aWithdrawalDto()
            .withWalletAddressFrom(walletFrom.getAddress())
            .withWalletAddressTo(walletTo.getAddress())
            .withCurrencyType(balanceFrom.getCurrency())
            .withAmount(BigDecimal.valueOf(10))
            .build();
        doReturn(user)
            .when(userService).get(any());
        doReturn(Optional.of(walletFrom))
            .when(walletRepository).findByAddressAndUserUsername(walletFrom.getAddress(), user.getUsername());
        doReturn(Optional.of(walletTo))
            .when(walletRepository).findByAddress(walletTo.getAddress());

        walletService.withdraw(withdrawalDto);

        verify(walletRepository).findByAddressAndUserUsername(walletFrom.getAddress(), user.getUsername());
        verify(walletRepository).findByAddress(walletTo.getAddress());
        assertThat(balanceTo.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));
    }

    @Test
    void shouldDepositForOrder() {
        var balance = BalanceTestDataBuilder.aBalance()
            .withCurrency(CurrencyType.EUR)
            .withAmount(BigDecimal.ZERO)
            .build();
        var wallet = WalletTestDataBuilder.aWallet()
            .withBalances(List.of(balance))
            .build();
        var user = UserTestDataBuilder.aUser()
            .withUsername(TEST_USERNAME)
            .withWallet(wallet)
            .build();
        doReturn(Optional.of(wallet))
            .when(walletRepository).findByAddressAndUserUsername(wallet.getAddress(), user.getUsername());
        walletService.depositForOrder(user, balance.getCurrency(), BigDecimal.valueOf(100));
        verify(walletRepository).findByAddressAndUserUsername(wallet.getAddress(), user.getUsername());
        assertThat(balance.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldWithdrawForOrder() {
        var balance = BalanceTestDataBuilder.aBalance()
            .withCurrency(CurrencyType.EUR)
            .withAmount(BigDecimal.TEN)
            .build();
        var walletFrom = WalletTestDataBuilder.aWallet()
            .withBalances(List.of(balance))
            .build();
        var walletTo = WalletTestDataBuilder.aWallet()
            .withBalances(List.of(balance))
            .build();
        var user = UserTestDataBuilder.aUser()
            .withUsername(TEST_USERNAME)
            .withWallet(walletFrom)
            .build();
        doReturn(Optional.of(walletFrom))
            .when(walletRepository).findByAddressAndUserUsername(walletFrom.getAddress(), user.getUsername());
        doReturn(Optional.of(walletTo))
            .when(walletRepository).findByAddress(any());

        walletService.withdrawForOrder(user, balance.getCurrency(), BigDecimal.valueOf(10));

        verify(walletRepository).findByAddressAndUserUsername(walletFrom.getAddress(), user.getUsername());
        assertThat(balance.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));
    }
}