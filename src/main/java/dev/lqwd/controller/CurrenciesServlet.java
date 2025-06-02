package dev.lqwd.controller;

import java.io.*;
import java.util.List;

import dev.lqwd.dto.CurrencyRequestDto;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dev.lqwd.dto.CurrencyResponseDto;

import dev.lqwd.service.CurrenciesService;
import dev.lqwd.utils.Validator;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currencies")
public class CurrenciesServlet extends BasicServlet {

    private final CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        List<CurrencyResponseDto> currenciesResponseDtos = currenciesService.getAll();

        doResponse(res, SC_OK, currenciesResponseDtos);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(
                req.getParameter("name"),
                req.getParameter("code"),
                req.getParameter("sign")
        );
        Validator.validate(currencyRequestDto);

        CurrencyResponseDto currencyResponseDto = currenciesService.save(currencyRequestDto);

        doResponse(res, SC_CREATED, currencyResponseDto);
    }

}