package com.malak.expense_tracker.service;

import com.malak.expense_tracker.model.User;
import jakarta.servlet.http.HttpServletResponse;

public interface ExpenseExportService {
    void exportAllToCSV(HttpServletResponse response) throws Exception;
    void exportAllToPDF(HttpServletResponse response) throws Exception;

    void exportUserToCSV(HttpServletResponse response, User user) throws Exception;
    void exportUserToPDF(HttpServletResponse response, User user) throws Exception;
}