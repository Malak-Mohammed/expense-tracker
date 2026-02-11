package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.ExpenseDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpenses(Authentication authentication) {
        String username = authentication.getName();
        List<ExpenseDTO> expenses = expenseService.getExpensesByUsername(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses retrieved successfully", expenses, HttpStatus.OK.value()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> addExpense(@RequestBody ExpenseDTO dto,
                                                              Authentication authentication) {
        String username = authentication.getName();
        ExpenseDTO savedExpense = expenseService.addExpense(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Expense added successfully", savedExpense, HttpStatus.CREATED.value()));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable Long expenseId,
                                                                 @RequestBody ExpenseDTO dto,
                                                                 Authentication authentication) {
        String username = authentication.getName();
        ExpenseDTO updatedExpense = expenseService.updateExpense(expenseId, dto, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense updated successfully", updatedExpense, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long expenseId,
                                                           Authentication authentication) {
        String username = authentication.getName();
        expenseService.deleteExpense(expenseId, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expense deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpenseById(@PathVariable Long expenseId,
                                                                  Authentication authentication) {
        String username = authentication.getName();
        return expenseService.getExpenseById(expenseId, username)
                .map(expense -> ResponseEntity.ok(new ApiResponse<>(true, "Expense found", expense, HttpStatus.OK.value())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Expense not found", null, HttpStatus.NOT_FOUND.value())));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByCategory(@PathVariable Long categoryId,
                                                                               Authentication authentication) {
        String username = authentication.getName();
        List<ExpenseDTO> expenses = expenseService.getExpensesByCategory(categoryId, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses retrieved successfully", expenses, HttpStatus.OK.value()));
    }

    @GetMapping("/between")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesBetweenDates(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            Authentication authentication) {
        String username = authentication.getName();
        List<ExpenseDTO> expenses = expenseService.getExpensesBetweenDates(username, start, end);
        return ResponseEntity.ok(new ApiResponse<>(true, "Expenses retrieved successfully", expenses, HttpStatus.OK.value()));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Double>> getTotalExpensesByUser(Authentication authentication) {
        String username = authentication.getName();
        Double total = expenseService.getTotalExpensesByUsername(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Total expenses calculated successfully", total, HttpStatus.OK.value()));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getMonthlyExpenseSummary(Authentication authentication) {
        String username = authentication.getName();
        Map<String, Double> summary = expenseService.getMonthlyExpenseSummary(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Monthly summary retrieved successfully", summary, HttpStatus.OK.value()));
    }
}