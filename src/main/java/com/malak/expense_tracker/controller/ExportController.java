package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.UserRepository;
import com.malak.expense_tracker.service.ExpenseExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExpenseExportService expenseExportService;
    private final UserRepository userRepository;

    public ExportController(ExpenseExportService expenseExportService, UserRepository userRepository) {
        this.expenseExportService = expenseExportService;
        this.userRepository = userRepository;
    }

    // Admin-only endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/csv/all")
    public void exportAllExpensesToCSV(HttpServletResponse response) throws Exception {
        expenseExportService.exportAllToCSV(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pdf/all")
    public void exportAllExpensesToPDF(HttpServletResponse response) throws Exception {
        expenseExportService.exportAllToPDF(response);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/csv")
    public void exportUserExpensesToCSV(HttpServletResponse response,
                                        @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) throws Exception {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        expenseExportService.exportUserToCSV(response, user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/pdf")
    public void exportUserExpensesToPDF(HttpServletResponse response,
                                        @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) throws Exception {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        expenseExportService.exportUserToPDF(response, user);
    }
}