package com.labanovich.cryptoexcange;

import org.springframework.boot.SpringApplication;

public class TestCryptoExcangeApplication {

    public static void main(String[] args) {
        SpringApplication.from(CryptoExcangeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
