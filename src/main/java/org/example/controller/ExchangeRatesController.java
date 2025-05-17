package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExchangePairDto;
import org.example.dto.ExchangeRateDto;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.controller.response_utils.ResponseUtils;
import org.example.service.ExchangeRatesService;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.example.handler.ErrorMessages.*;

@WebServlet("/exchangeRates/*")
public class ExchangeRatesController extends HttpServlet{

    private ExchangeRatesService exchangeRatesService;

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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if(pathInfo != null) {
                ResponseUtils.sendError(res, INCORRECT_PATH_VARIABLES.getMessage(), SC_BAD_REQUEST);
            }
            List<ExchangeRateDto> exRatesDto = exchangeRatesService.getAll();
            String jsonResponse = ResponseUtils.toJson(exRatesDto);
            ResponseUtils.sendResponse(res, jsonResponse, SC_OK);
        } catch (BadRequestException e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        } catch (NotFoundException e){
            ResponseUtils.sendError(res, MOT_EXIST_CURRENCY.getMessage() + " " + e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            ResponseUtils.sendError(res, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null){
            ResponseUtils.sendError(res, INCORRECT_URL.getMessage(), SC_BAD_REQUEST);
            return;
        }
        String baseCurrency = req.getParameter("baseCurrencyCode");
        String targetCurrency = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        try {
            ExchangePairDto pairDto = new ExchangePairDto();
            pairDto.setPair(baseCurrency, targetCurrency);
            pairDto.setRate(rate);
            ExchangeRateDto exRateDto = exchangeRatesService.save(pairDto);
            String json = ResponseUtils.toJson(exRateDto);
            ResponseUtils.sendResponse(res, json, SC_ACCEPTED);
        } catch (BadRequestException e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        } catch (NotFoundException e) {
            ResponseUtils.sendError(res, MOT_EXIST_CURRENCY.getMessage() + " " + e.getMessage(), e.getStatusCode());
        } catch (ExistInDbException e) {
            ResponseUtils.sendError(res, EXIST_PAIR.getMessage(), e.getStatusCode());
        } catch (Exception e){
            ResponseUtils.sendError(res, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

}