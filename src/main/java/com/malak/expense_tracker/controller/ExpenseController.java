package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.ExpenseDTO;
import com.malak.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Create Expense
    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO savedExpense = expenseService.addExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    // Update Expense
    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long expenseId,
                                                    @RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO savedExpense = expenseService.updateExpense(expenseId, expenseDTO);
        return ResponseEntity.ok(savedExpense);
    }

    // Get Expense by ID
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable Long expenseId) {
        return expenseService.getExpenseById(expenseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Expense
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    // Get Expenses by User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUser(userId));
    }

    // Get Expenses by Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(expenseService.getExpensesByCategory(categoryId));
    }

    // Get Expenses Between Dates
    @GetMapping("/user/{userId}/between")
    public ResponseEntity<List<ExpenseDTO>> getExpensesBetweenDates(
            @PathVariable Long userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(expenseService.getExpensesBetweenDates(userId, start, end));
    }

    // Get Total Expenses by User
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalExpensesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getTotalExpensesByUser(userId));
    }

    // Get Monthly Expense Summary
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Double>> getMonthlyExpenseSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getMonthlyExpenseSummary(userId));
    }
}