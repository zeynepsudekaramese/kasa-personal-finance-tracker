package com.kasa.api.service;

import com.kasa.api.exception.AccountNotFoundException;
import com.kasa.api.model.Account;
import com.kasa.api.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AccountService {

    private static final Set<String> VALID_TYPES = Set.of("CASH", "BANK", "CARD");

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getById(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account create(String name, String type) {
        validateType(type);
        return accountRepository.insert(name, type);
    }

    public Account update(int id, String name, String type) {
        validateType(type);
        int updatedRows = accountRepository.update(id, name, type);
        if (updatedRows == 0) {
            throw new AccountNotFoundException(id);
        }
        return getById(id);
    }

    public void delete(int id) {
        int deletedRows = accountRepository.deleteById(id);
        if (deletedRows == 0) {
            throw new AccountNotFoundException(id);
        }
    }

    private void validateType(String type) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("type must be one of " + VALID_TYPES + ", got: " + type);
        }
    }
}