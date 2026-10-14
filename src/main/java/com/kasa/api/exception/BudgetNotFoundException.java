package com.kasa.api.exception;

public class BudgetNotFoundException extends RuntimeException {
    public BudgetNotFoundException(int id) {
        super("Budget not found with id: " + id);
    }
    public BudgetNotFoundException(String message) {
    super(message);
    }
}