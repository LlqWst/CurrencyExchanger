package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExchangePairDto;
import org.example.dto.ExchangeRateDto;
import org.example.exceptions.CurrenciesExceptions;
import org.example.exceptions.custom_exceptions.BadRequestException;
import org.example.response_utils.ResponseUtils;
import org.example.service.ExchangeRatesService;
import org.example.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.example.exceptions.ErrorMessages.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet{

    private ExchangeRatesService exchangeRateService;
    private Validator validator;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            if ("GET".equalsIgnoreCase(req.getMethod())) {
                doGet(req, resp);
            } else if ("PATCH".equalsIgnoreCase(req.getMethod())) {
                doPatch(req, resp);
            } else {
                ResponseUtils.sendError(resp, INCORRECT_METHOD.getMessage(), SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e){
            ResponseUtils.sendError(resp, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void init(){
        this.exchangeRateService = new ExchangeRatesService();
        this.validator = new Validator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if(pathInfo == null || pathInfo.equals("/")) {
                throw new BadRequestException(MISSING_PAIR.getMessage());
            }
            String pair = pathInfo.split("/")[1];

            validator.validateParameter(pair, "pair");
            validator.validatePair(pair);

            ExchangeRateDto exRateDto = exchangeRateService.get(pair);
            ResponseUtils.sendJson(res, exRateDto, SC_OK);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

    @Override
    protected void doPatch (HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if(pathInfo == null || pathInfo.equals("/")){
                throw new BadRequestException(MISSING_PAIR.getMessage());
            }
            String pair = pathInfo.split("/")[1];

            String encryptedRate = req.getReader()
                    .lines()
                    .collect(Collectors.joining());

            validator.validateParameter(pair, "pair");
            validator.validatePair(pair);

            validator.validateParameter(encryptedRate, "rate");
            BigDecimal rate = validator.patchParsRate(encryptedRate);

            ExchangePairDto pairDto = new ExchangePairDto();
            pairDto.setPair(pair);
            pairDto.setRate(rate);
            ExchangeRateDto exRateDto = exchangeRateService.update(pairDto);
            ResponseUtils.sendJson(res, exRateDto, SC_OK);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

}