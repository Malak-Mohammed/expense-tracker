package com.malak.expense_tracker.response;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class ApiError {
    private final boolean success;
    private final String message;
    private final String cause;
    private final int status;
    private final LocalDateTime timestamp;

    public ApiError(String message, String cause, HttpStatus status) {
        this.success = false;
        this.message = message;
        this.cause = cause;
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getCause() { return cause; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}