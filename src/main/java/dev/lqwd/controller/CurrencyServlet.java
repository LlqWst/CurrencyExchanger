package dev.lqwd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.CurrencyResponseDto;
import dev.lqwd.service.CurrenciesService;
import dev.lqwd.utils.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private CurrenciesService currenciesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {

        this.currenciesService = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String code = req.getPathInfo().replaceFirst("/", "");
        Validator.validate(code);

        CurrencyResponseDto responseDto = currenciesService.getByCode(code);

        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), responseDto);
    }

}