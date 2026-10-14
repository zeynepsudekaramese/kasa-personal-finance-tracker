package com.kasa.api.service;

import com.kasa.api.exception.CategoryNotFoundException;
import com.kasa.api.model.Category;
import com.kasa.api.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAll_returnsAllCategoriesFromRepository() {
        List<Category> categories = List.of(
                new Category(1, "Salary", "INCOME"),
                new Category(2, "Rent", "EXPENSE")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();

        assertEquals(2, result.size());
        assertEquals(categories, result);
    }

    @Test
    void getById_whenFound_returnsCategory() {
        Category category = new Category(1, "Salary", "INCOME");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(1);

        assertEquals(category, result);
    }

    @Test
    void getById_whenNotFound_throwsCategoryNotFoundException() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(99));
    }

    @Test
    void create_withValidType_insertsAndReturnsCategory() {
        Category created = new Category(1, "Groceries", "EXPENSE");
        when(categoryRepository.insert("Groceries", "EXPENSE")).thenReturn(created);

        Category result = categoryService.create("Groceries", "EXPENSE");

        assertEquals(created, result);
        verify(categoryRepository).insert("Groceries", "EXPENSE");
    }

    @Test
    void create_withInvalidType_throwsException_andNeverCallsRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.create("Groceries", "SAVINGS"));

        verify(categoryRepository, never()).insert(anyString(), anyString());
    }

    @Test
    void update_whenFound_returnsUpdatedCategory() {
        when(categoryRepository.update(1, "Rent", "EXPENSE")).thenReturn(1);

        Category result = categoryService.update(1, "Rent", "EXPENSE");

        assertEquals(new Category(1, "Rent", "EXPENSE"), result);
    }

    @Test
    void update_whenNotFound_throwsCategoryNotFoundException() {
        when(categoryRepository.update(99, "Rent", "EXPENSE")).thenReturn(0);

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.update(99, "Rent", "EXPENSE"));
    }

    @Test
    void delete_whenFound_callsRepositoryDelete() {
        when(categoryRepository.deleteById(1)).thenReturn(1);

        categoryService.delete(1);

        verify(categoryRepository).deleteById(1);
    }

    @Test
    void delete_whenNotFound_throwsCategoryNotFoundException() {
        when(categoryRepository.deleteById(99)).thenReturn(0);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(99));
    }
}