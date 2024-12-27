package com.labanovich.crypto.exchange.service;

import com.labanovich.crypto.exchange.dto.DepositDto;
import com.labanovich.crypto.exchange.dto.WalletDto;
import com.labanovich.crypto.exchange.dto.WithdrawalDto;
import com.labanovich.crypto.exchange.entity.Balance;
import com.labanovich.crypto.exchange.entity.User;
import com.labanovich.crypto.exchange.entity.Wallet;
import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.mapper.WalletMapper;
import com.labanovich.crypto.exchange.repository.WalletRepository;
import com.labanovich.crypto.exchange.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletService {

    private final UserService userService;
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional
    public void create() {
        var user = userService.get(SecurityUtil.getCurrentUsername());
        var wallet = initWallet(user);
        walletRepository.save(wallet);
    }

    public List<WalletDto> getWallets() {
        return walletRepository.findAllByUserUsername(SecurityUtil.getCurrentUsername()).stream()
            .map(walletMapper::map)
            .toList();
    }

    private Wallet initWallet(User user) {
        var wallet = new Wallet();
        Arrays.stream(CurrencyType.values())
            .map(currencyType -> Balance.builder()
                .amount(BigDecimal.ZERO)
                .currency(currencyType)
                .build())
            .forEach(wallet::addBalance);
        wallet.addUser(user);
        wallet.setAddress(String.valueOf(UUID.randomUUID()));
        return wallet;
    }

    @Transactional
    public void deposit(DepositDto depositDto) {
        var user = userService.get(SecurityUtil.getCurrentUsername());
        var balanceForUpdate = walletRepository.findByAddressAndUserUsername(depositDto.getWalletAddressTo(),
                user.getUsername())
            .map(wallet -> {
                var balanceOptional = wallet.getBalances().stream()
                    .filter(balance -> balance.getCurrency() == depositDto.getCurrencyType())
                    .findFirst();
                return balanceOptional
                    .orElse(null);
            })
            .orElseThrow();
        balanceForUpdate.deposit(depositDto.getAmount());
    }

    @Transactional
    public void withdraw(WithdrawalDto withdrawalDto) {
        var user = userService.get(SecurityUtil.getCurrentUsername());
        var balanceForWithdrawal = walletRepository.findByAddressAndUserUsername(withdrawalDto.getWalletAddressFrom(), user.getUsername())
            .map(wallet -> wallet.getBalanceForWithdrawal(withdrawalDto.getCurrencyType(), withdrawalDto.getAmount()))
            .orElseThrow();
        balanceForWithdrawal.withdrawal(withdrawalDto.getAmount());
        var balanceForDeposit = walletRepository.findByAddress(withdrawalDto.getWalletAddressTo())
            .map(wallet -> wallet.getBalanceForDeposit(withdrawalDto.getCurrencyType()))
            .orElseThrow();
        balanceForDeposit.deposit(withdrawalDto.getAmount());
    }

    @Transactional
    public void depositForOrder(User user, CurrencyType currencyType, BigDecimal amount) {
        var balanceForUpdate = walletRepository.findByAddressAndUserUsername(user.getWallet().getAddress(), user.getUsername())
            .map(wallet -> {
                var balanceOptional = wallet.getBalances().stream()
                    .filter(balance -> balance.getCurrency() == currencyType)
                    .findFirst();
                return balanceOptional
                    .orElse(null);
            })
            .orElseThrow();
        balanceForUpdate.deposit(amount);
    }

    @Transactional
    public void withdrawForOrder(User user, CurrencyType currencyType, BigDecimal amount) {
        var walletAddress = user.getWallet().getAddress();
        var balanceForWithdrawal = walletRepository.findByAddressAndUserUsername(walletAddress, user.getUsername())
            .map(wallet -> wallet.getBalanceForWithdrawal(currencyType, amount))
            .orElseThrow();
        balanceForWithdrawal.withdrawal(amount);
        var balanceForDeposit = walletRepository.findByAddress(walletAddress)
            .map(wallet -> wallet.getBalanceForDeposit(currencyType))
            .orElseThrow();
        balanceForDeposit.deposit(amount);
    }
}
