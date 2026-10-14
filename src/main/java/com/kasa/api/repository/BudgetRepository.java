package com.kasa.api.repository;

import com.kasa.api.model.Budget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class BudgetRepository {

    private final JdbcTemplate jdbcTemplate;

    public BudgetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Budget> ROW_MAPPER = (rs, rowNum) -> new Budget(
            rs.getInt("id"),
            rs.getInt("category_id"),
            rs.getBigDecimal("monthly_limit")
    );

    public List<Budget> findAll() {
        return jdbcTemplate.query("SELECT id, category_id, monthly_limit FROM budgets ORDER BY id", ROW_MAPPER);
    }

    public Optional<Budget> findById(int id) {
        List<Budget> results = jdbcTemplate.query(
                "SELECT id, category_id, monthly_limit FROM budgets WHERE id = ?", ROW_MAPPER, id);
        return results.stream().findFirst();
    }

    public Optional<Budget> findByCategoryId(int categoryId) {
        List<Budget> results = jdbcTemplate.query(
                "SELECT id, category_id, monthly_limit FROM budgets WHERE category_id = ?", ROW_MAPPER, categoryId);
        return results.stream().findFirst();
    }

    public Budget insert(int categoryId, BigDecimal monthlyLimit) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO budgets (category_id, monthly_limit) VALUES (?, ?) RETURNING id, category_id, monthly_limit",
                ROW_MAPPER, categoryId, monthlyLimit);
    }

    public int update(int id, BigDecimal monthlyLimit) {
        return jdbcTemplate.update(
                "UPDATE budgets SET monthly_limit = ? WHERE id = ?",
                monthlyLimit, id);
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM budgets WHERE id = ?", id);
    }
}