package com.malak.expense_tracker.exception;

public class InvalidExpenseException extends ApiException {
    public InvalidExpenseException(String message) {
        super(message);
    }
    public InvalidExpenseException(String message, Throwable cause) { super(message, cause); }

}
