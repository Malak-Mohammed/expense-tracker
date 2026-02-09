package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.model.Expense;
import com.malak.expense_tracker.repository.ExpenseRepository;
import com.malak.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense addExpense(Expense expense) {
        if (expense.getExpenseAmount() == null || expense.getExpenseAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        if (expense.getExpenseDate() == null || expense.getExpenseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Expense date cannot be in the future");
        }
        return expenseRepository.save(expense);
    }

    @Override
    public Expense updateExpense(Long expenseId, Expense updatedExpense) {
        Expense existing = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        existing.setExpenseAmount(updatedExpense.getExpenseAmount());
        existing.setExpenseDate(updatedExpense.getExpenseDate());
        existing.setExpenseDescription(updatedExpense.getExpenseDescription());
        existing.setCategory(updatedExpense.getCategory());

        return expenseRepository.save(existing);
    }

    @Override
    public void deleteExpense(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("Expense not found");
        }
        expenseRepository.deleteById(expenseId);
    }

    @Override
    public Optional<Expense> getExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId);
    }

    @Override
    public List<Expense> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserUserId(userId);
    }

    @Override
    public List<Expense> getExpensesByCategory(Long categoryId) {
        return expenseRepository.findByCategoryCategoryId(categoryId);
    }

    @Override
    public List<Expense> getExpensesBetweenDates(Long userId, LocalDate start, LocalDate end) {
        return expenseRepository.findAll().stream()
                .filter(exp -> exp.getUser().getUserId().equals(userId))
                .filter(exp -> !exp.getExpenseDate().isBefore(start) && !exp.getExpenseDate().isAfter(end))
                .toList();
    }

    @Override
    public Double getTotalExpensesByUser(Long userId) {
        return getExpensesByUser(userId).stream()
                .mapToDouble(Expense::getExpenseAmount)
                .sum();
    }

    @Override
    public Map<String, Double> getMonthlyExpenseSummary(Long userId) {

        List<Expense> expenses = expenseRepository.findByUserUserId(userId);


        return expenses.stream()
                .collect(Collectors.groupingBy(
                        exp -> exp.getExpenseDate().getYear() + "-" + exp.getExpenseDate().getMonthValue(),
                        Collectors.summingDouble(Expense::getExpenseAmount)
                ));
    }
}