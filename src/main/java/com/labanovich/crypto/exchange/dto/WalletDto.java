package com.labanovich.crypto.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {

    private Long id;
    private String address;
    private LocalDateTime createdDatetime;
    private List<BalanceDto> balances;
}