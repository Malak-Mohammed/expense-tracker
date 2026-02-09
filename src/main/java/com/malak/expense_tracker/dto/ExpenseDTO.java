package com.malak.expense_tracker.dto;

import java.time.LocalDate;

public record ExpenseDTO(Long expenseId,
                         Double expenseAmount,
                         LocalDate expenseDate,
                         String expenseDescription,
                         Long userId,
                         Long categoryId) {
}
