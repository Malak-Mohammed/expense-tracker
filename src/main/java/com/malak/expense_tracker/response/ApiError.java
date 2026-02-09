package com.malak.expense_tracker.response;

import org.springframework.http.HttpStatus;

public class ApiError {
    private final String message;
    private final String cause;
    private final HttpStatus status;

    public ApiError(String message, Throwable throwable, HttpStatus status) {
        this.message = message;
        this.cause = throwable != null ? throwable.toString() : null;
        this.status = status;
    }

    public String getMessage() { return message; }
    public String getCause() { return cause; }
    public HttpStatus getStatus() { return status; }
}