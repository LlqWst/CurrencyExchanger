package dev.lqwd.controller;

import java.io.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.CurrencyRequestDto;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dev.lqwd.dto.CurrencyResponseDto;

import dev.lqwd.service.CurrenciesService;
import dev.lqwd.utils.Validator;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private CurrenciesService currenciesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {

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

        CurrencyRequestDto requestDto = new CurrencyRequestDto(
                req.getParameter("name"),
                req.getParameter("code"),
                req.getParameter("sign")
        );
        Validator.validate(requestDto);

        CurrencyResponseDto ResponseDto = currenciesService.save(requestDto);
        res.setStatus(SC_CREATED);
        objectMapper.writeValue(res.getWriter(), ResponseDto);
    }

}