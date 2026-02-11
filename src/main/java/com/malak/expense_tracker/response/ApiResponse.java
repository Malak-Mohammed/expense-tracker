package com.malak.expense_tracker.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String cause;
    private T data;
    private LocalDateTime timestamp;
    private int status;

    public ApiResponse(boolean success, String message, String cause, T data, int status) {
        this.success = success;
        this.message = message;
        this.cause = cause;
        this.data = data;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data, int status) {
        this(success, message, null, data, status);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getCause() { return cause; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setCause(String cause) { this.cause = cause; }
    public void setData(T data) { this.data = data; }
    public void setStatus(int status) { this.status = status; }
}