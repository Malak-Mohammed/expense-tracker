package com.malak.expense_tracker.exception;

public class DuplicateUsernameException extends ApiException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
    public DuplicateUsernameException(String message, Throwable cause) { super(message, cause); }

}
