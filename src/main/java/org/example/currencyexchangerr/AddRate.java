package org.example.currencyexchangerr;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "AddRate", value = "/add-rate")
public class AddRate extends HttpServlet {
    private static final String DB_URL = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int base;
        int target;
        double rate;
        long rate_2;

        try {
            base = Integer.parseInt(request.getParameter("base"));
            target = Integer.parseInt(request.getParameter("target"));
            rate = Double.parseDouble(request.getParameter("rate"));
            rate_2 = (long) (rate * 1_000_000);
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, base);
                pstmt.setInt(2, target);
                pstmt.setLong(3, rate_2);
                pstmt.executeUpdate();
            }
            response.sendRedirect("all-rates");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Database error: " + e.getMessage());
        }
    }
}