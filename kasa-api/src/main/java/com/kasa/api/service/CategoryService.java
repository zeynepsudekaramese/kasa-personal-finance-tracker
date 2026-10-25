package com.kasa.api.service;

import com.kasa.api.exception.CategoryNotFoundException;
import com.kasa.api.model.Category;
import com.kasa.api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {

          return categoryRepository.findAll();  }

    public Category getById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category create(String name, String type) {
        validateType(type);
        return categoryRepository.insert(name, type);
    }

    public Category update(int id, String name, String type) {
        validateType(type);
        int updatedRows = categoryRepository.update(id, name, type);
        if (updatedRows == 0) {
            throw new CategoryNotFoundException(id);
        }
        return new Category(id, name, type);
    }

    public void delete(int id) {
        int deletedRows = categoryRepository.deleteById(id);
        if (deletedRows == 0) {
            throw new CategoryNotFoundException(id);
        }
    }

    private void validateType(String type) {
        if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
            throw new IllegalArgumentException("type must be INCOME or EXPENSE, got: " + type);
        }
    }
}