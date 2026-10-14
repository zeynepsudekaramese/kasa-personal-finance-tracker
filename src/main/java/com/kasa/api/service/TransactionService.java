package com.kasa.api.service;

import com.kasa.api.exception.AccountNotFoundException;
import com.kasa.api.exception.CategoryNotFoundException;
import com.kasa.api.exception.TransactionNotFoundException;
import com.kasa.api.model.Transaction;
import com.kasa.api.repository.AccountRepository;
import com.kasa.api.repository.CategoryRepository;
import com.kasa.api.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TransactionService {

    private static final Set<String> VALID_TYPES = Set.of("INCOME", "EXPENSE");

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                               AccountRepository accountRepository,
                               CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction getById(int id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public Transaction create(int accountId, int categoryId, BigDecimal amount, String type,
                               String description, LocalDate occurredOn) {
        validateType(type);
        validateAmount(amount);
        validateAccountExists(accountId);
        validateCategoryExists(categoryId);
        return transactionRepository.insert(accountId, categoryId, amount, type, description, occurredOn);
    }

    public Transaction update(int id, int accountId, int categoryId, BigDecimal amount, String type,
                              String description, LocalDate occurredOn) {
        validateType(type);
        validateAmount(amount);
        validateAccountExists(accountId);
        validateCategoryExists(categoryId);
        int updatedRows = transactionRepository.update(id, accountId, categoryId, amount, type, description, occurredOn);
        if (updatedRows == 0) {
            throw new TransactionNotFoundException(id);
        }
        return getById(id);
    }

    public void delete(int id) {
        int deletedRows = transactionRepository.deleteById(id);
        if (deletedRows == 0) {
            throw new TransactionNotFoundException(id);
        }
    }

    private void validateType(String type) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("type must be one of " + VALID_TYPES + ", got: " + type);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }
    }

    private void validateAccountExists(int accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private void validateCategoryExists(int categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}