package com.amblessed.chinookapi.controller;



/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 02-Sep-25
 */

import com.amblessed.chinookapi.entity.QueryRequest;
import com.amblessed.chinookapi.repository.SQLExecutor;
import com.amblessed.chinookapi.service.OpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/sql")
public class SQLController {

    private final OpenAIService openAIService;
    private final SQLExecutor sqlExecutor;

    public SQLController(OpenAIService openAIService, SQLExecutor sqlExecutor) {
        this.openAIService = openAIService;
        this.sqlExecutor = sqlExecutor;
    }

    @PostMapping("/query")
    public ResponseEntity<Object> query(@RequestBody Map<String, String> body) {

        try {
            String userInput = body.get("request");
            String sql = openAIService.generateSQL(userInput);

            // Remove leading/trailing whitespace and newlines
            sql = sql.trim();

            // VERY IMPORTANT: validate the SQL before running (to avoid DROP TABLE, DELETE, etc.)
            int selectIndex = sql.toLowerCase().indexOf("select");
            if(selectIndex >= 0) {
                sql = sql.substring(selectIndex).trim();
            } else {
                return ResponseEntity.badRequest().body("No valid SELECT statement found.");
            }

            sql = convertColumnsToSnakeCase(sql);

            List<Object[]> results = sqlExecutor.runQuery(sql);
            return ResponseEntity.ok(results);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // Simple example function to convert some known columns to snake_case
    private String convertColumnsToSnakeCase(String sql) {

        sql = sql.replaceAll("(?i)```", "");

        sql = sql.replaceAll("(?i)Customers", "customer");
        sql = sql.replaceAll("(?i)Invoices", "invoice");
        sql = sql.replaceAll("(?i)Albums", "album");

        sql = sql.replaceAll("(?i)CustomerId", "customer_id");
        sql = sql.replaceAll("(?i)FirstName", "first_name");
        sql = sql.replaceAll("(?i)LastName", "last_name");
        sql = sql.replaceAll("(?i)Total", "total");
        sql = sql.replaceAll("(?i)InvoiceId", "invoice_id");
        return sql;
    }
}
