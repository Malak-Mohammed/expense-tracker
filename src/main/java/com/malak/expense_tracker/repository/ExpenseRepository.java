package com.malak.expense_tracker.repository;

import com.malak.expense_tracker.model.Expense;
import com.malak.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


    List<Expense> findByUser(User user);

    List<Expense> findByCategoryCategoryIdAndUser(Long categoryId, User user);

    List<Expense> findByUserAndExpenseDateBetween(User user, LocalDate start, LocalDate end);
}