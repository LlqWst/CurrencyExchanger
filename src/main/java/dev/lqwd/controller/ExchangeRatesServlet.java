package dev.lqwd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.exceptions.MethodNotAllowedException;
import dev.lqwd.utility.Parser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.service.ExchangeRatesService;
import dev.lqwd.utility.Validator;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet{

    private ExchangeRatesService exchangeRatesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if ("GET".equalsIgnoreCase(req.getMethod())) {
            doGet(req, resp);
        } else if ("POST".equalsIgnoreCase(req.getMethod())) {
            doPost(req, resp);
        }
        throw new MethodNotAllowedException("Only GET and POST methods are allowed");
    }

    @Override
    public void init(){

        this.exchangeRatesService = new ExchangeRatesService();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        Validator.validatePathVariable(req.getPathInfo());

        List<ExchangeRateResponseDto> exRatesDto = exchangeRatesService.getAll();

        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), exRatesDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        Validator.validatePathVariable(req.getPathInfo());

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        Validator.validate(baseCurrencyCode, targetCurrencyCode);
        Validator.validateParameter(rate);

        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto (
                baseCurrencyCode,
                targetCurrencyCode,
                Parser.parsRate(rate)
        );
        ExchangeRateResponseDto responseDto = exchangeRatesService.save(requestDto);

        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), responseDto);
    }

}