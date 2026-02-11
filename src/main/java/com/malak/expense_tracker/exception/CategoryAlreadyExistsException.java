package com.malak.expense_tracker.exception;

public class CategoryAlreadyExistsException extends ApiException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
    public CategoryAlreadyExistsException(String message, Throwable cause) { super(message, cause); }

}
