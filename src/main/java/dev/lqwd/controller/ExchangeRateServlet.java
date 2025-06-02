package dev.lqwd.controller;

import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.utils.Parser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.service.ExchangeRatesService;
import dev.lqwd.utils.Validator;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BasicServlet {

    private static final String COMMA = "%2C";
    private final ExchangeRatesService exchangeRateService = new ExchangeRatesService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getMethod().equalsIgnoreCase("PATCH")){
            this.doPatch(req, resp);
        }
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String currencyPair = getPathParameter(req);

        Validator.validateCurrencyPairLength(currencyPair);

        String from = currencyPair.substring(0, 3);
        String to = currencyPair.substring(3);
        Validator.validate(from, to);

        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.getByPair(from, to);

        doResponse(res, SC_OK, exchangeRateResponseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String currencyPair = getPathParameter(req);

        Validator.validateCurrencyPairLength(currencyPair);

        String from = currencyPair.substring(0, 3);
        String to = currencyPair.substring(3);

        Validator.validate(from, to);

        String rate = req.getReader()
                .readLine()
                .replace("rate=", "")
                .replace(COMMA, ".");

        Validator.validateParameter(rate, "rate");

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(
                from,
                to,
                Parser.parsRate(rate)
        );

        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.update(exchangeRateRequestDto);

        doResponse(res, SC_OK, exchangeRateResponseDto);
    }

}