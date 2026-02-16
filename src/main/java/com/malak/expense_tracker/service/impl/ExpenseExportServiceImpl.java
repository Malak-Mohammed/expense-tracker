package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.model.Expense;
import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.ExpenseRepository;
import com.malak.expense_tracker.service.ExpenseExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ExpenseExportServiceImpl implements ExpenseExportService {

    private final ExpenseRepository expenseRepository;

    public ExpenseExportServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void exportAllToCSV(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.csv");

        List<Expense> expenses = expenseRepository.findAll();
        writeCsv(response, expenses);
    }

    @Override
    public void exportUserToCSV(HttpServletResponse response, User user) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.csv");

        List<Expense> expenses = expenseRepository.findByUser(user);
        writeCsv(response, expenses);
    }

    private void writeCsv(HttpServletResponse response, List<Expense> expenses) throws Exception {
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Description,Amount,Category,Date");
            for (Expense expense : expenses) {
                writer.printf("%d,%s,%.2f,%s,%s%n",
                        expense.getExpenseId(),
                        expense.getExpenseDescription(),
                        expense.getExpenseAmount(),
                        expense.getCategory().getCategoryName(),
                        expense.getExpenseDate().toString());
            }
        }
    }

    @Override
    public void exportAllToPDF(HttpServletResponse response) throws Exception {
        exportToPDF(response, expenseRepository.findAll(), "All Expenses Report");
    }

    @Override
    public void exportUserToPDF(HttpServletResponse response, User user) throws Exception {
        exportToPDF(response, expenseRepository.findByUser(user), "User Expense Report");
    }

    private void exportToPDF(HttpServletResponse response, List<Expense> expenses, String title) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));

        PdfPTable table = new PdfPTable(5);
        table.addCell("ID");
        table.addCell("Description");
        table.addCell("Amount");
        table.addCell("Category");
        table.addCell("Date");

        for (Expense expense : expenses) {
            table.addCell(String.valueOf(expense.getExpenseId()));
            table.addCell(expense.getExpenseDescription());
            table.addCell(String.valueOf(expense.getExpenseAmount()));
            table.addCell(expense.getCategory().getCategoryName());
            table.addCell(expense.getExpenseDate().toString());
        }

        document.add(table);
        document.close();
    }
}