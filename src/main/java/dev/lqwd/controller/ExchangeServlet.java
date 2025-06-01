package dev.lqwd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ExchangeRequestDto;
import dev.lqwd.utils.Parser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.dto.ExchangeResponseDto;
import dev.lqwd.service.ExchangeService;
import dev.lqwd.utils.Validator;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private ExchangeService exchangeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        this.exchangeService = new ExchangeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        Validator.validate(from, to);
        Validator.validateParameter(amount, "amount");

        ExchangeRequestDto requestDto = new ExchangeRequestDto(
                from,
                to,
                Parser.parsAmount(amount)
        );
        ExchangeResponseDto responseDto = exchangeService.get(requestDto);

        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), responseDto);
    }

}