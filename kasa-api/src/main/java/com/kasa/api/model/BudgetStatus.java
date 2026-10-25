package com.kasa.api.model;

import java.math.BigDecimal;

public record BudgetStatus(
        Integer categoryId,
        BigDecimal monthlyLimit,
        BigDecimal amountSpent,
        BigDecimal remaining,
        boolean overBudget
) {
}