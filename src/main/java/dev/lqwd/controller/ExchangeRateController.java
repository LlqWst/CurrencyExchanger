package dev.lqwd.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.dto.ExchangePairDto;
import dev.lqwd.dto.ExchangeRateDto;
import dev.lqwd.exceptions.CurrenciesExceptions;
import dev.lqwd.exceptions.custom_exceptions.BadRequestException;
import dev.lqwd.response_utils.ResponseUtils;
import dev.lqwd.service.ExchangeRatesService;
import dev.lqwd.validator.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.*;
import static dev.lqwd.exceptions.ErrorMessages.*;

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
            validator.validatePairVariable(pathInfo);
            String pair = pathInfo.split("/")[1];

            validator.validateParameter(pair);
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
            validator.validatePairVariable(pathInfo);
            String pair = pathInfo.split("/")[1];

            String encryptedRate = req.getReader()
                    .lines()
                    .collect(Collectors.joining());

            validator.validateParameter(pair);
            validator.validatePair(pair);

            BigDecimal rate = validator.patchParsRate(encryptedRate);

            ExchangePairDto pairDto = new ExchangePairDto();
            pairDto.setBaseCurrencyCode(pair.substring(0, 3));
            pairDto.setTargetCurrencyCode(pair.substring(3, 6));
            pairDto.setRate(rate);
            ExchangeRateDto exRateDto = exchangeRateService.update(pairDto);
            ResponseUtils.sendJson(res, exRateDto, SC_OK);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

}