package org.example.currencyexchangerr;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/add-currency")
public class AddCurrency extends HttpServlet {
    private static final String DB_URL = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, code);
                pstmt.setString(2, fullName);
                pstmt.setString(3, sign);
                pstmt.executeUpdate();
            }

            // Перенаправляем пользователя после успешной вставки
            response.sendRedirect("all-currencies"); // Или другой страницы
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Database error: " + e.getMessage());
        }
    }
}