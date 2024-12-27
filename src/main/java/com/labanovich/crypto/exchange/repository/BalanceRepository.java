package com.labanovich.crypto.exchange.repository;

import com.labanovich.crypto.exchange.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
