package com.kasa.api.exception;

public class CategoryNotFoundException extends RuntimeException{
	public CategoryNotFoundException(int id) {
		super("Category not found with id:" +id);
	}
}
