package com.malak.expense_tracker.exception;

public class ExpenseNotFoundException extends ApiException {
    public ExpenseNotFoundException(String message) {
        super(message);
    }

    public ExpenseNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
