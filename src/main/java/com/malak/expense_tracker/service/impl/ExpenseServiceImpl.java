package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.dto.ExpenseDTO;
import com.malak.expense_tracker.exception.ExpenseNotFoundException;
import com.malak.expense_tracker.exception.UserNotFoundException;
import com.malak.expense_tracker.exception.CategoryNotFoundException;
import com.malak.expense_tracker.exception.InvalidExpenseException;
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
    public ExpenseDTO addExpense(ExpenseDTO dto, String username) {
        validateExpense(dto);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + dto.categoryId()));

        Expense expense = ExpenseMapper.toEntity(dto, user, category);
        Expense saved = expenseRepository.save(expense);
        return ExpenseMapper.toDto(saved);
    }

    @Override
    public ExpenseDTO updateExpense(Long expenseId, ExpenseDTO dto, String username) {
        Expense existing = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + expenseId));

        validateExpense(dto);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + dto.categoryId()));

        existing.setExpenseAmount(dto.expenseAmount());
        existing.setExpenseDate(dto.expenseDate());
        existing.setExpenseDescription(dto.expenseDescription());
        existing.setUser(user);
        existing.setCategory(category);

        Expense saved = expenseRepository.save(existing);
        return ExpenseMapper.toDto(saved);
    }

    @Override
    public void deleteExpense(Long expenseId, String username) {
        Expense existing = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + expenseId));

        if (!existing.getUser().getUsername().equals(username)) {
            throw new ExpenseNotFoundException("Expense not found for user: " + username);
        }

        expenseRepository.delete(existing);
    }

    @Override
    public Optional<ExpenseDTO> getExpenseById(Long expenseId, String username) {
        return expenseRepository.findById(expenseId)
                .filter(exp -> exp.getUser().getUsername().equals(username))
                .map(ExpenseMapper::toDto);
    }

    @Override
    public List<ExpenseDTO> getExpensesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return expenseRepository.findByUser(user)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByCategory(Long categoryId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return expenseRepository.findByCategoryCategoryIdAndUser(categoryId, user)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesBetweenDates(String username, LocalDate start, LocalDate end) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return expenseRepository.findByUserAndExpenseDateBetween(user, start, end)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalExpensesByUsername(String username) {
        return getExpensesByUsername(username).stream()
                .mapToDouble(ExpenseDTO::expenseAmount)
                .sum();
    }

    @Override
    public Map<String, Double> getMonthlyExpenseSummary(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        List<Expense> expenses = expenseRepository.findByUser(user);

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        exp -> exp.getExpenseDate().getYear() + "-" + exp.getExpenseDate().getMonthValue(),
                        Collectors.summingDouble(Expense::getExpenseAmount)
                ));
    }

    private void validateExpense(ExpenseDTO dto) {
        if (dto.expenseAmount() == null || dto.expenseAmount() < 0) {
            throw new InvalidExpenseException("Expense amount must be positive");
        }
        if (dto.expenseDate() == null || dto.expenseDate().isAfter(LocalDate.now())) {
            throw new InvalidExpenseException("Expense date cannot be in the future");
        }
    }
}