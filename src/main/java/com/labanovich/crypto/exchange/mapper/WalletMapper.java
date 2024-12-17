package com.labanovich.crypto.exchange.mapper;

import com.labanovich.crypto.exchange.dto.WalletDto;
import com.labanovich.crypto.exchange.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDto map(Wallet wallet);
}
