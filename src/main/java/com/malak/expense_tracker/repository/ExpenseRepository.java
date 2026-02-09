package com.malak.expense_tracker.repository;

import com.malak.expense_tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserUserId(Long userId);
    List<Expense> findByCategoryCategoryId(Long categoryId);

}
