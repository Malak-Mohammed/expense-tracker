package com.malak.expense_tracker.service;

import com.malak.expense_tracker.dto.ExpenseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseService {


    ExpenseDTO addExpense(ExpenseDTO expenseDTO, String username);


    ExpenseDTO updateExpense(Long expenseId, ExpenseDTO updatedExpenseDTO, String username);


    void deleteExpense(Long expenseId, String username);


    Optional<ExpenseDTO> getExpenseById(Long expenseId, String username);


    List<ExpenseDTO> getExpensesByUsername(String username);


    List<ExpenseDTO> getExpensesByCategory(Long categoryId, String username);


    List<ExpenseDTO> getExpensesBetweenDates(String username, LocalDate start, LocalDate end);


    Double getTotalExpensesByUsername(String username);


    Map<String, Double> getMonthlyExpenseSummary(String username);
}