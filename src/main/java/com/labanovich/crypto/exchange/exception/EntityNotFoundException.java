package com.labanovich.crypto.exchange.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String label, Object value) {
        super(String.format("Couldn't find entity with %s = %s", label, value));
    }
}
