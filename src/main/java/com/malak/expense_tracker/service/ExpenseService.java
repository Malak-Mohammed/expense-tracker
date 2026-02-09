package com.malak.expense_tracker.service;

import com.malak.expense_tracker.dto.ExpenseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseService {

    ExpenseDTO addExpense(ExpenseDTO expenseDTO);

    ExpenseDTO updateExpense(Long expenseId, ExpenseDTO updatedExpenseDTO);

    void deleteExpense(Long expenseId);

    Optional<ExpenseDTO> getExpenseById(Long expenseId);

    List<ExpenseDTO> getExpensesByUser(Long userId);

    List<ExpenseDTO> getExpensesByCategory(Long categoryId);

    List<ExpenseDTO> getExpensesBetweenDates(Long userId, LocalDate start, LocalDate end);

    Double getTotalExpensesByUser(Long userId);

    Map<String, Double> getMonthlyExpenseSummary(Long userId);
}