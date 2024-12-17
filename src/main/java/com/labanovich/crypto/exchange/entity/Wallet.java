package com.labanovich.crypto.exchange.entity;

import com.labanovich.crypto.exchange.enums.CurrencyType;
import com.labanovich.crypto.exchange.exception.EntityNotFoundException;
import com.labanovich.crypto.exchange.exception.WithdrawalProcessException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String address;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdDatetime;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<Balance> balances = new ArrayList<>();

    public void addBalance(Balance balance) {
        balance.setWallet(this);
        balances.add(balance);
    }

    public void addUser(User user) {
        user.setWallet(this);
        this.user = user;
    }

    public Balance getBalanceForWithdrawal(CurrencyType currencyType, BigDecimal amount) {
        return balances.stream()
            .filter(balance -> balance.getCurrency() == currencyType)
            .filter(balance -> balance.getAmount().compareTo(amount) >= 0)
            .findFirst()
            .orElseThrow(() -> new WithdrawalProcessException("Balance is less than amount"));
    }

    public Balance getBalanceForDeposit(CurrencyType currencyType) {
        return balances.stream()
            .filter(balance -> balance.getCurrency() == currencyType)
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("id", currencyType));
    }
}