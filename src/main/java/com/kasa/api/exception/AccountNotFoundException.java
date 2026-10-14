package com.kasa.api.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(int id) {
        super("Account not found with id: " + id);
    }
}