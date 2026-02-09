package com.malak.expense_tracker.service;

import com.malak.expense_tracker.model.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseService {
    Expense addExpense(Expense expense);
    Expense updateExpense(Long expenseId, Expense updatedExpense);
    void deleteExpense(Long expenseId);
    Optional<Expense> getExpenseById(Long expenseId);
    List<Expense> getExpensesByUser(Long userId);
    List<Expense> getExpensesByCategory(Long categoryId);
    List<Expense> getExpensesBetweenDates(Long userId, LocalDate start, LocalDate end);
    Double getTotalExpensesByUser(Long userId);
    Map<String, Double> getMonthlyExpenseSummary(Long userId);




}
