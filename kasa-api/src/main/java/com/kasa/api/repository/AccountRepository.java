package com.kasa.api.repository;

import com.kasa.api.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Account> ROW_MAPPER = (rs, rowNum) -> new Account(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getObject("created_at", LocalDateTime.class)
    );

    public List<Account> findAll() {
        return jdbcTemplate.query("SELECT id, name, type, created_at FROM accounts ORDER BY name", ROW_MAPPER);
    }

    public Optional<Account> findById(int id) {
        List<Account> results = jdbcTemplate.query(
                "SELECT id, name, type, created_at FROM accounts WHERE id = ?", ROW_MAPPER, id);
        return results.stream().findFirst();
    }

    public Account insert(String name, String type) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO accounts (name, type) VALUES (?, ?) RETURNING id, name, type, created_at",
                ROW_MAPPER, name, type);
    }

    public int update(int id, String name, String type) {
        return jdbcTemplate.update(
                "UPDATE accounts SET name = ?, type = ? WHERE id = ?",
                name, type, id);
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM accounts WHERE id = ?", id);
    }
}