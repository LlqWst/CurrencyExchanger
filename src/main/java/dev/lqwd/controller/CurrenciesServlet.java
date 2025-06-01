package dev.lqwd.controller;

import java.io.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.exceptions.MethodNotAllowedException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dev.lqwd.dto.CurrencyResponseDto;

import dev.lqwd.service.CurrenciesService;
import dev.lqwd.utility.Validator;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet{

    private CurrenciesService currenciesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {

        if ("GET".equalsIgnoreCase(req.getMethod())) {
            doGet(req, res);
        } else if ("POST".equalsIgnoreCase(req.getMethod())) {
            doPost(req, res);
        }
        throw new MethodNotAllowedException("Only GET and POST methods are allowed");
    }

    @Override
    public void init(){

        this.currenciesService = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        List<CurrencyResponseDto> currenciesResponseDto = currenciesService.getAll();

        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), currenciesResponseDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String pathInfo = req.getPathInfo();
        Validator.validatePathVariable(pathInfo);

        CurrencyRequestDto requestDto = new CurrencyRequestDto(
                req.getParameter("name"),
                req.getParameter("code"),
                req.getParameter("sign")
        );
        Validator.validate(requestDto);

        CurrencyResponseDto ResponseDto = currenciesService.save(requestDto);
        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), ResponseDto);
    }

}