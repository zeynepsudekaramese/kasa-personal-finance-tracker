package com.kasa.api.service;

import com.kasa.api.exception.BudgetNotFoundException;
import com.kasa.api.exception.CategoryNotFoundException;
import com.kasa.api.model.Budget;
import com.kasa.api.model.BudgetStatus;
import com.kasa.api.repository.BudgetRepository;
import com.kasa.api.repository.CategoryRepository;
import com.kasa.api.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(BudgetRepository budgetRepository,
                          CategoryRepository categoryRepository,
                          TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }

    public Budget getById(int id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
    }

    public Budget create(int categoryId, BigDecimal monthlyLimit) {
        validateLimit(monthlyLimit);
        validateCategoryExists(categoryId);
        return budgetRepository.insert(categoryId, monthlyLimit);
    }

    public Budget update(int id, BigDecimal monthlyLimit) {
        validateLimit(monthlyLimit);
        int updatedRows = budgetRepository.update(id, monthlyLimit);
        if (updatedRows == 0) {
            throw new BudgetNotFoundException(id);
        }
        return getById(id);
    }

    public void delete(int id) {
        int deletedRows = budgetRepository.deleteById(id);
        if (deletedRows == 0) {
            throw new BudgetNotFoundException(id);
        }
    }

    public BudgetStatus getStatusForCurrentMonth(int categoryId) {
        validateCategoryExists(categoryId);

        Budget budget = budgetRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new BudgetNotFoundException(
                        "No budget defined for category id: " + categoryId));

        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        BigDecimal spent = transactionRepository.sumAmountByCategoryAndDateRange(categoryId, start, end);
        BigDecimal remaining = budget.monthlyLimit().subtract(spent);
        boolean overBudget = remaining.compareTo(BigDecimal.ZERO) < 0;

        return new BudgetStatus(categoryId, budget.monthlyLimit(), spent, remaining, overBudget);
    }

    private void validateLimit(BigDecimal monthlyLimit) {
        if (monthlyLimit == null || monthlyLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("monthlyLimit must be greater than 0");
        }
    }

    private void validateCategoryExists(int categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}