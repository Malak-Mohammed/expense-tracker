package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.dto.ExpenseDTO;
import com.malak.expense_tracker.mapper.ExpenseMapper;
import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.model.Expense;
import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.CategoryRepository;
import com.malak.expense_tracker.repository.ExpenseRepository;
import com.malak.expense_tracker.repository.UserRepository;
import com.malak.expense_tracker.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ExpenseDTO addExpense(ExpenseDTO dto) {
        validateExpense(dto);

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Expense expense = ExpenseMapper.toEntity(dto, user, category);
        Expense saved = expenseRepository.save(expense);
        return ExpenseMapper.toDto(saved);
    }

    @Override
    public ExpenseDTO updateExpense(Long expenseId, ExpenseDTO dto) {
        Expense existing = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        validateExpense(dto);

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existing.setExpenseAmount(dto.expenseAmount());
        existing.setExpenseDate(dto.expenseDate());
        existing.setExpenseDescription(dto.expenseDescription());
        existing.setUser(user);
        existing.setCategory(category);

        Expense saved = expenseRepository.save(existing);
        return ExpenseMapper.toDto(saved);
    }

    @Override
    public void deleteExpense(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("Expense not found");
        }
        expenseRepository.deleteById(expenseId);
    }

    @Override
    public Optional<ExpenseDTO> getExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId).map(ExpenseMapper::toDto);
    }

    @Override
    public List<ExpenseDTO> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserUserId(userId)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByCategory(Long categoryId) {
        return expenseRepository.findByCategoryCategoryId(categoryId)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesBetweenDates(Long userId, LocalDate start, LocalDate end) {
        return expenseRepository.findAll().stream()
                .filter(exp -> exp.getUser().getUserId().equals(userId))
                .filter(exp -> !exp.getExpenseDate().isBefore(start) && !exp.getExpenseDate().isAfter(end))
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalExpensesByUser(Long userId) {
        return getExpensesByUser(userId).stream()
                .mapToDouble(ExpenseDTO::expenseAmount)
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

    private void validateExpense(ExpenseDTO dto) {
        if (dto.expenseAmount() == null || dto.expenseAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        if (dto.expenseDate() == null || dto.expenseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Expense date cannot be in the future");
        }
    }
}