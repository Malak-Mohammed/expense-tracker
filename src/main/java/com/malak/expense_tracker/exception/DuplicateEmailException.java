package com.malak.expense_tracker.exception;

public class DuplicateEmailException extends ApiException {
    public DuplicateEmailException(String message) {
        super(message);
    }
    public DuplicateEmailException(String message, Throwable cause) { super(message, cause); }

}
