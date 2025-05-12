package org.example.currencyexchangerr;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "UpdateRate", value = "/update-rate")
public class UpdateRate extends HttpServlet {

    //http://localhost:8080/update-rate?base=1&target=2&rate=4.44

    private static final String DB_URL = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        }
        resp.sendError(405, "Only PATCH method supported");
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int base;
        int target;
        double rate;

        try {
            base = Integer.parseInt(request.getParameter("base"));
            target = Integer.parseInt(request.getParameter("target"));
            rate = Double.parseDouble(request.getParameter("rate"));
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "UPDATE ExchangeRates SET rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, rate);
                pstmt.setInt(2, base);
                pstmt.setInt(3, target);
                int affectedValues = pstmt.executeUpdate();

                if(affectedValues == 0){
                    response.sendError(404, "Currency pair not found");
                    return;
                }
            }

            response.sendRedirect("all-rates");
            response.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Database error: " + e.getMessage());
        }
    }

}