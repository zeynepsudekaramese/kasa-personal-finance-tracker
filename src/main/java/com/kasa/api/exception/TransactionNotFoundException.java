package com.kasa.api.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(int id) {
        super("Transaction not found with id: " + id);
    }
}