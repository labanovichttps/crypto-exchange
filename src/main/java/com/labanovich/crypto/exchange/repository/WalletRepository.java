package com.labanovich.crypto.exchange.repository;

import com.labanovich.crypto.exchange.entity.Wallet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @EntityGraph(attributePaths = "balances")
    List<Wallet> findAllByUserUsername(String username);

    @EntityGraph(attributePaths = "balances")
    Optional<Wallet> findByAddress(String address);

    @EntityGraph(attributePaths = "balances")
    Optional<Wallet> findByAddressAndUserUsername(String address, String username);
}
