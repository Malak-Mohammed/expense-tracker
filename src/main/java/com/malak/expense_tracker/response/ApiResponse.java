package com.malak.expense_tracker.response;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String cause; // optional: for debugging or extra context
    private T data;


    public ApiResponse(boolean success, String message, String cause, T data) {
        this.success = success;
        this.message = message;
        this.cause = cause;
        this.data = data;
    }


    public ApiResponse(boolean success, String message, T data) {
        this(success, message, null, data);
    }


    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getCause() { return cause; }
    public T getData() { return data; }


    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setCause(String cause) { this.cause = cause; }
    public void setData(T data) { this.data = data; }
}