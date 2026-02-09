package com.malak.expense_tracker.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
    public CategoryAlreadyExistsException(String message, Throwable cause) { super(message, cause); }

}
