package dev.lqwd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.exceptions.BadRequestException;
import dev.lqwd.utility.Parser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.service.ExchangeRatesService;
import dev.lqwd.utility.Validator;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getMethod().equalsIgnoreCase("PATCH")){
            this.doPatch(req, resp);
        }
        super.service(req, resp);
    }

    private static final String INCORRECT_PAIR = "Currency pair should be equals 6 letters";
    private ExchangeRatesService exchangeRateService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {

        this.exchangeRateService = new ExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String pair = req.getPathInfo().replaceFirst("/", "");

        if (pair.length() != 6) {
            throw new BadRequestException(INCORRECT_PAIR);
        }

        String from = pair.substring(0, 3);
        String to = pair.substring(3);
        Validator.validate(from, to);

        ExchangeRateResponseDto responseDto = exchangeRateService.getByPair(from, to);
        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), responseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String pair = req.getPathInfo().replaceFirst("/", "");

        if (pair.length() != 6) {
            throw new BadRequestException(INCORRECT_PAIR);
        }

        String from = pair.substring(0, 3);
        String to = pair.substring(3);

        String rate = req.getReader()
                .readLine()
                .replace("rate=", "")
                .replace("%2C", ".");

        Validator.validate(from, to);
        Validator.validateParameter(rate, "rate");

        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(
                from,
                to,
                Parser.parsRate(rate)
        );

        ExchangeRateResponseDto responseDto = exchangeRateService.update(requestDto);
        res.setStatus(SC_OK);
        objectMapper.writeValue(res.getWriter(), responseDto);
    }

}