package com.malak.expense_tracker.exception;

public class CategoryNotFoundException extends ApiException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
