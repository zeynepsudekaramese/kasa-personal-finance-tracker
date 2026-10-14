package com.kasa.api.model;

import java.math.BigDecimal;

public record Budget(Integer id, Integer categoryId, BigDecimal monthlyLimit) {
}