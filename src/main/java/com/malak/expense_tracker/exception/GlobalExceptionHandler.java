package com.malak.expense_tracker.exception;

import com.malak.expense_tracker.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ===== USER EXCEPTIONS =====
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiError> handleDuplicateEmail(DuplicateEmailException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiError> handleDuplicateUsername(DuplicateUsernameException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    // ===== EXPENSE EXCEPTIONS =====
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ApiError> handleExpenseNotFound(ExpenseNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidExpenseException.class)
    public ResponseEntity<ApiError> handleInvalidExpense(InvalidExpenseException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    // ===== CATEGORY EXCEPTIONS =====
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    // ===== FALLBACKS =====
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage(), ex.getCause(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(
                new ApiError("An unexpected error occurred", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}