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
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static dev.lqwd.exceptions.ErrorMessages.*;

@WebServlet("/exchangeRates/*")
public class ExchangeRatesController extends HttpServlet{

    private ExchangeRatesService exchangeRatesService;
    private Validator validator;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            if ("GET".equalsIgnoreCase(req.getMethod())) {
                doGet(req, resp);
            } else if ("POST".equalsIgnoreCase(req.getMethod())) {
                doPost(req, resp);
            } else {
                ResponseUtils.sendError(resp, INCORRECT_METHOD.getMessage(), SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e){
            ResponseUtils.sendError(resp, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void init(){
        this.exchangeRatesService = new ExchangeRatesService();
        this.validator = new Validator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            validator.validatePathVariable(pathInfo);
            List<ExchangeRateDto> exRatesDto = exchangeRatesService.getAll();
            ResponseUtils.sendJson(res, exRatesDto, SC_OK);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            validator.validatePathVariable(pathInfo);
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rateEncrypted = req.getParameter("rate");

            validator.validateParameter(baseCurrencyCode);
            validator.validateParameter(targetCurrencyCode);
            validator.validatePair(baseCurrencyCode, targetCurrencyCode);

            validator.validateParameter(rateEncrypted);
            BigDecimal rate = validator.parsRate(rateEncrypted);

            ExchangePairDto exPairDto = new ExchangePairDto();
            exPairDto.setBaseCurrencyCode(baseCurrencyCode);
            exPairDto.setTargetCurrencyCode(targetCurrencyCode);
            exPairDto.setRate(rate);

            ExchangeRateDto exRateDto = exchangeRatesService.save(exPairDto);
            ResponseUtils.sendJson(res, exRateDto, SC_CREATED);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

}