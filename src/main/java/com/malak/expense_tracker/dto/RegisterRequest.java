package com.malak.expense_tracker.dto;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String role
) {}