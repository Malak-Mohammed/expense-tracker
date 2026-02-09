package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.ExpenseDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.service.ExpenseService;
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

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> addExpense(@RequestBody ExpenseDTO dto) {
        ExpenseDTO savedExpense = expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Expense added successfully", savedExpense));
    }


    @PutMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable Long expenseId,
                                                                 @RequestBody ExpenseDTO dto) {
        ExpenseDTO updatedExpense = expenseService.updateExpense(expenseId, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense updated successfully", updatedExpense));
    }


    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense deleted successfully", null));
    }


    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpenseById(@PathVariable Long expenseId) {
        return expenseService.getExpenseById(expenseId)
                .map(expense -> ResponseEntity.ok(new ApiResponse<>(true, "Expense found", expense)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Expense not found", null)));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByUser(@PathVariable Long userId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses retrieved successfully", expenses));
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByCategory(@PathVariable Long categoryId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses retrieved successfully", expenses));
    }


    @GetMapping("/user/{userId}/between")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesBetweenDates(
            @PathVariable Long userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        List<ExpenseDTO> expenses = expenseService.getExpensesBetweenDates(userId, start, end);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses retrieved successfully", expenses));
    }


    @GetMapping("/user/{userId}/total")
    public ResponseEntity<ApiResponse<Double>> getTotalExpensesByUser(@PathVariable Long userId) {
        Double total = expenseService.getTotalExpensesByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Total expenses calculated successfully", total));
    }


    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getMonthlyExpenseSummary(@PathVariable Long userId) {
        Map<String, Double> summary = expenseService.getMonthlyExpenseSummary(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Monthly summary retrieved successfully", summary));
    }
}