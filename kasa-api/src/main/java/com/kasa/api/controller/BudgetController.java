package com.kasa.api.controller;

import com.kasa.api.exception.BudgetNotFoundException;
import com.kasa.api.exception.CategoryNotFoundException;
import com.kasa.api.model.Budget;
import com.kasa.api.model.BudgetRequest;
import com.kasa.api.model.BudgetStatus;
import com.kasa.api.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public List<Budget> getAll() {
        return budgetService.getAll();
    }

    @GetMapping("/{id}")
    public Budget getById(@PathVariable int id) {
        return budgetService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Budget create(@RequestBody BudgetRequest request) {
        return budgetService.create(request.categoryId(), request.monthlyLimit());
    }

    @PutMapping("/{id}")
    public Budget update(@PathVariable int id, @RequestBody BudgetRequest request) {
        return budgetService.update(id, request.monthlyLimit());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        budgetService.delete(id);
    }

    @GetMapping("/category/{categoryId}/status")
    public BudgetStatus getStatus(@PathVariable int categoryId) {
        return budgetService.getStatusForCurrentMonth(categoryId);
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<String> handleBudgetNotFound(BudgetNotFoundException e) {
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