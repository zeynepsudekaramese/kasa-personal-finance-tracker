package com.kasa.api.repository;

import com.kasa.api.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Transaction> ROW_MAPPER = (rs, rowNum) -> new Transaction(
            rs.getInt("id"),
            rs.getInt("account_id"),
            rs.getInt("category_id"),
            rs.getBigDecimal("amount"),
            rs.getString("type"),
            rs.getString("description"),
            rs.getObject("occurred_on", LocalDate.class),
            rs.getObject("created_at", LocalDateTime.class)
    );

    public List<Transaction> findAll() {
        return jdbcTemplate.query(
                "SELECT id, account_id, category_id, amount, type, description, occurred_on, created_at " +
                        "FROM transactions ORDER BY occurred_on DESC",
                ROW_MAPPER);
    }

    public Optional<Transaction> findById(int id) {
        List<Transaction> results = jdbcTemplate.query(
                "SELECT id, account_id, category_id, amount, type, description, occurred_on, created_at " +
                        "FROM transactions WHERE id = ?",
                ROW_MAPPER, id);
        return results.stream().findFirst();
    }

    public Transaction insert(int accountId, int categoryId, BigDecimal amount, String type,
                               String description, LocalDate occurredOn) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO transactions (account_id, category_id, amount, type, description, occurred_on) " +
                        "VALUES (?, ?, ?, ?, ?, ?) " +
                        "RETURNING id, account_id, category_id, amount, type, description, occurred_on, created_at",
                ROW_MAPPER, accountId, categoryId, amount, type, description, occurredOn);
    }

    public int update(int id, int accountId, int categoryId, BigDecimal amount, String type,
                       String description, LocalDate occurredOn) {
        return jdbcTemplate.update(
                "UPDATE transactions SET account_id = ?, category_id = ?, amount = ?, type = ?, " +
                        "description = ?, occurred_on = ? WHERE id = ?",
                accountId, categoryId, amount, type, description, occurredOn, id);
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM transactions WHERE id = ?", id);
        
    }
    public BigDecimal sumAmountByCategoryAndDateRange(int categoryId, LocalDate start, LocalDate end) {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM transactions " +
                        "WHERE category_id = ? AND type = 'EXPENSE' AND occurred_on BETWEEN ? AND ?",
                BigDecimal.class, categoryId, start, end);
    }
    
}