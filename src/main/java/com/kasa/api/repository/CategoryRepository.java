package com.kasa.api.repository;

import com.kasa.api.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Category> ROW_MAPPER = (rs, rowNum) -> new Category(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("type")
    );

    public List<Category> findAll() {
        return jdbcTemplate.query("SELECT id, name, type FROM categories ORDER BY name", ROW_MAPPER);
    }

    public Optional<Category> findById(int id) {
        List<Category> results = jdbcTemplate.query(
                "SELECT id, name, type FROM categories WHERE id = ?", ROW_MAPPER, id);
        return results.stream().findFirst();
    }

    public Category insert(String name, String type) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO categories (name, type) VALUES (?, ?) RETURNING id",
                Integer.class, name, type);
        return new Category(id, name, type);
    }

    public int update(int id, String name, String type) {
        return jdbcTemplate.update(
                "UPDATE categories SET name = ?, type = ? WHERE id = ?",
                name, type, id);
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM categories WHERE id = ?", id);
    }
}