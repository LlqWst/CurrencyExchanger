package org.example.currencyexchangerr;



import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "UpdateRate", value = "/update-rate")
public class UpdateRate extends HttpServlet {

    //http://localhost:8080/update-rate?base=1&target=2&rate=4.44

    private static final String DB_URL = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";

//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        String body = req.getReader().lines().collect(Collectors.joining());
//        System.out.println(body);
//        //req.setCharacterEncoding("charset=UTF-8");
//        //req.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
//            doPatch(req, resp);
//        }
//        resp.sendError(405, "Only PATCH method supported");
//    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String body = request.getReader().lines().collect(Collectors.joining());
        System.out.println(body);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int base;
        int target;
        double rate;

        try {
            String str1 = request.getParameter("base");
            String str2 = request.getParameter("target");
            String str3 = request.getParameter("rate");
            System.out.println(str1 + str2 + str3);
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