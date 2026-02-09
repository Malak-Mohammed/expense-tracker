package com.malak.expense_tracker.mapper;

import com.malak.expense_tracker.dto.ExpenseDTO;
import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.model.Expense;
import com.malak.expense_tracker.model.User;

public class ExpenseMapper {

    public static ExpenseDTO toDto(Expense expense) {
        return new ExpenseDTO(
                expense.getExpenseId(),
                expense.getExpenseAmount(),
                expense.getExpenseDate(),
                expense.getExpenseDescription(),
                expense.getUser().getUserId(),
                expense.getCategory().getCategoryId()
        );
    }

    public static Expense toEntity(ExpenseDTO dto, User user, Category category) {
        Expense expense = new Expense();
        expense.setExpenseId(dto.expenseId());
        expense.setExpenseAmount(dto.expenseAmount());
        expense.setExpenseDate(dto.expenseDate());
        expense.setExpenseDescription(dto.expenseDescription());
        expense.setUser(user);
        expense.setCategory(category);
        return expense;
    }

}
