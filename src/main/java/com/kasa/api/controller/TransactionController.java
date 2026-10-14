package com.kasa.api.controller;

import com.kasa.api.exception.AccountNotFoundException;
import com.kasa.api.exception.CategoryNotFoundException;
import com.kasa.api.exception.TransactionNotFoundException;
import com.kasa.api.model.Transaction;
import com.kasa.api.model.TransactionRequest;
import com.kasa.api.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable int id) {
        return transactionService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody TransactionRequest request) {
        return transactionService.create(
                request.accountId(),
                request.categoryId(),
                request.amount(),
                request.type(),
                request.description(),
                request.occurredOn()
        );
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable int id, @RequestBody TransactionRequest request) {
        return transactionService.update(
                id,
                request.accountId(),
                request.categoryId(),
                request.amount(),
                request.type(),
                request.description(),
                request.occurredOn()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        transactionService.delete(id);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleTransactionNotFound(TransactionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFound(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}