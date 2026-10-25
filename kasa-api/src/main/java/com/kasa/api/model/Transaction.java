package com.kasa.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Transaction(
        Integer id,
        Integer accountId,
        Integer categoryId,
        BigDecimal amount,
        String type,
        String description,
        LocalDate occurredOn,
        LocalDateTime createdAt
) {
}