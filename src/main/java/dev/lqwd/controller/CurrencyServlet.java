package dev.lqwd.controller;

import dev.lqwd.dto.CurrencyResponseDto;
import dev.lqwd.service.CurrenciesService;
import dev.lqwd.utils.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currency/*")
public class CurrencyServlet extends BasicServlet {

    private final CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String code = getPathParameter(req);
        Validator.validate(code);

        CurrencyResponseDto currencyResponseDto = currenciesService.getByCode(code);

        doResponse(res, SC_OK, currencyResponseDto);
    }

}