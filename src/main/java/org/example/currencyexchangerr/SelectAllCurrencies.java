package org.example.currencyexchangerr;

import java.io.*;
import java.sql.*;

import DataSource.CurrenciesListener;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

//http://localhost:8080/hello-servlet

@WebServlet(name = "AllCurrencies", value = "/all-currencies")
public class SelectAllCurrencies extends HttpServlet {

    private String message;
    //private static final String DB_URL = "jdbc:sqlite:C:/Programming/Maven/projects/CurrencyExchanger/db/exchanger.db";
    @Override
    public void init() {
        message = "All Currency";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }


        response.setContentType("text/html");
        try (Connection conn = CurrenciesListener.getConnection()) {

            String query = "SELECT ID, Code, FullName, Sign FROM Currencies";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");

            out.print("<h4>");
            while (rs.next()){
                int id = rs.getInt("ID");
                String code = rs.getString("Code");
                String fullName = rs.getString("FullName");
                String sign = rs.getString("Sign");
                out.printf("ID:%d | Code:%s | FullName:%s | Sign:%s</br>", id, code, fullName, sign);
            }
            out.print("</h4>");

            out.println("<a href= /> Menu </a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Database error: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {}
}