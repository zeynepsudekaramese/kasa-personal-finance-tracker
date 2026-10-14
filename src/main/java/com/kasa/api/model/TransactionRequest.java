package com.kasa.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        Integer accountId,
        Integer categoryId,
        BigDecimal amount,
        String type,
        String description,
        LocalDate occurredOn
) {
}