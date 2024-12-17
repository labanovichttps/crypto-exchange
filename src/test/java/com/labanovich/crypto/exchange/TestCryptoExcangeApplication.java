package com.labanovich.crypto.exchange;

import org.springframework.boot.SpringApplication;

public class TestCryptoExcangeApplication {

    public static void main(String[] args) {
        SpringApplication.from(CryptoExchangeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
