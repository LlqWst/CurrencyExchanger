package dev.lqwd.controller;

import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.utils.Parser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.service.ExchangeRatesService;
import dev.lqwd.utils.Validator;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BasicServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        List<ExchangeRateResponseDto> exRatesDtos = exchangeRatesService.getAll();

        doResponse(res, SC_OK, exRatesDtos);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        Validator.validate(baseCurrencyCode, targetCurrencyCode);
        Validator.validateParameter(rate, "rate");

        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(
                baseCurrencyCode,
                targetCurrencyCode,
                Parser.parsRate(rate)
        );
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.save(requestDto);

        doResponse(res, SC_CREATED, exchangeRateResponseDto);
    }

}