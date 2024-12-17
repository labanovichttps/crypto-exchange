package com.labanovich.crypto.exchange.controller;

import com.labanovich.crypto.exchange.dto.DepositDto;
import com.labanovich.crypto.exchange.dto.WalletDto;
import com.labanovich.crypto.exchange.dto.WithdrawalDto;
import com.labanovich.crypto.exchange.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Void> createWallet() {
        walletService.create();
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @GetMapping
    public ResponseEntity<List<WalletDto>> getWallets() {
        return ResponseEntity.ok(walletService.getWallets());
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositDto depositDto) {
        walletService.deposit(depositDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal(@RequestBody WithdrawalDto withdrawalDto) {
        walletService.withdraw(withdrawalDto);
        return ResponseEntity.ok().build();
    }
}
