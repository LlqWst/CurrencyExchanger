package org.example.currencyexchangerr;

import java.io.*;
import java.sql.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "SelectAllRates", value = "/all-rates")
public class SelectAllRates extends HttpServlet {
    private static final String DB_URL = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String sql = "SELECT ID, BaseCurrencyId, TargetCurrencyId, Rate FROM ExchangeRates";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + "All Rates" + "</h1>");

            out.print("<h4>");
            while (rs.next()){
                int id = rs.getInt("ID");
                String baseCurrencyId = rs.getString("BaseCurrencyID");
                String targetCurrencyId = rs.getString("TargetCurrencyID");
                Double rate = (double) rs.getLong("Rate") / 1000000;
                out.printf("ID:%d | Base:%s | Target:%s | Rate:%f</br>", id, baseCurrencyId, targetCurrencyId, rate);
            }
            out.print("</h4>");

            out.println("<a href= /> Menu </a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Database error: " + e.getMessage());
        }
    }

}