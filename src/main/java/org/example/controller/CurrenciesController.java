package org.example.controller;

import java.io.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.dto.CurrencyDto;
import org.example.entity.Currency;
import org.example.handler.BadRequestException;
import org.example.handler.CurrencyExistException;
import org.example.handler.NotFoundException;
import org.example.service.CurrenciesService;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currencies/*")
public class CurrenciesController extends HttpServlet{

    private CurrenciesService currenciesService;

    @Override
    public void init(){
        this.currenciesService = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if(pathInfo == null) {
                List<Currency> currencies = currenciesService.getAll();
                String jsonResponse = toJson(currencies);
                outputResponse(response, jsonResponse, SC_OK);
                return;
            }
            if (pathInfo.equals("/")) {
                throw new BadRequestException("The currency code is missing in url");
            }
            String code = pathInfo.split("/")[1];
            CurrencyDto dto = new CurrencyDto();
            dto.setCode(code);
            Currency currency = currenciesService.get(dto);
            String jsonResponse = toJson(currency);
            outputResponse(response, jsonResponse, SC_OK);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if(pathInfo != null){
            throw new BadRequestException("Incorrect URL");
        }
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        if(name == null || code == null || sign == null){
            throw new BadRequestException("""
        The required form field is missing. Three fields are required to create a currency: 'name', 'code', 'sign'""");
        }
        try {
            CurrencyDto dto = new CurrencyDto();
            dto.setName(name);
            dto.setCode(code);
            dto.setSign(sign);
            Currency currency = currenciesService.save(dto);
            String json = toJson(currency);
            outputResponse(response, json, SC_ACCEPTED);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (CurrencyExistException e){
            throw new CurrencyExistException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    private void outputResponse(HttpServletResponse response, String payload, int status){
        try {
            response.setStatus(status);
            if (payload != null){
                PrintWriter pw = response.getWriter();
                pw.write(payload);
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