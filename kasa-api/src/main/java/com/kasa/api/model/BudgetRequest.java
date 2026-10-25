package com.kasa.api.model;

import java.math.BigDecimal;

public record BudgetRequest(Integer categoryId, BigDecimal monthlyLimit) {
}