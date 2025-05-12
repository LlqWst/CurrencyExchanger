package org.example.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.entity.Currency;
import org.example.dao.CurrenciesDao;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currencies/*")
public class CurrenciesController extends HttpServlet{

    private CurrenciesDao currenciesDao;

    public CurrenciesController(){
        this.currenciesDao = new CurrenciesDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonResponse = null;
        String pathInfo = request.getPathInfo();
        if(pathInfo == null || pathInfo.equals("/")) {
            try {
                List<Currency> currencies = currenciesDao.getAll();
                jsonResponse = toJson(currencies);
            } catch (Exception e) {
                String errorMessage = "{\"message:\" \"база данных недоступна\"}";
                outputResponse(response, errorMessage, 500);
            }
        } else {
            String str = pathInfo.split("/")[1];
            try {
                Currency currency = currenciesDao.get(str);
                jsonResponse = toJson(currency);
            } catch (SQLException e) {
                String errorMessage = "{\"message:\" \"такой валюты нет\"}";
                outputResponse(response, errorMessage, 500);
            }
        }
        outputResponse(response, jsonResponse, SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("code", code);
        json.put("fullName", fullName);
        json.put("sign", sign);

        String reqBody = mapper.writeValueAsString(json);

        int rc = SC_OK;
        boolean res = this.currenciesDao.createCurrency(reqBody);
        if(!res){
            rc = HttpServletResponse.SC_BAD_REQUEST;
        }
        this.outputResponse(response, null, rc);
    }

    private void outputResponse(HttpServletResponse response, String payload, int status){
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.setStatus(status);
            if (payload != null){
                PrintWriter pw = response.getWriter();
                pw.println(payload);
                pw.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJson(Object list){
        if(list == null){
            return null;
        }
        String json;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(list);
        } catch (Exception e){
            throw new RuntimeException();
        }
        return json;
    }

}