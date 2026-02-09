package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.model.Expense;
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

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    @PutMapping("/{expenseId")
    public ResponseEntity<Expense>  updateExpense(@RequestBody Expense expense, @PathVariable Long expenseId) {
        Expense savedExpense = expenseService.updateExpense(expenseId, expense);
        return ResponseEntity.status(HttpStatus.OK).body(savedExpense);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getExpense(@PathVariable Long expenseId) {
        return expenseService.getExpenseById(expenseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUser(userId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(expenseService.getExpensesByCategory(categoryId));
    }

    @GetMapping("/user/{userId}/between")
    public ResponseEntity<List<Expense>> getExpensesBetweenDates(
            @PathVariable Long userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(expenseService.getExpensesBetweenDates(userId, start, end));
    }

    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalExpensesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getTotalExpensesByUser(userId));
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Double>> getMonthlyExpenseSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getMonthlyExpenseSummary(userId));
    }






}
