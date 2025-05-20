package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.controller.response_utils.ResponseUtils;
import org.example.dto.ExchangeDto;
import org.example.dto.ExchangePairDto;
import org.example.handler.CurrenciesExceptions;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.service.ExchangeService;
import org.example.validation.Validator;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.example.handler.ErrorMessages.*;

@WebServlet("/exchange/*")
public class ExchangeController extends HttpServlet{

    private ExchangeService exchangeService;
    private Validator validator;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            if ("GET".equalsIgnoreCase(req.getMethod())) {
                doGet(req, resp);
            } else {
                ResponseUtils.sendError(resp, INCORRECT_METHOD.getMessage(), SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e){
            ResponseUtils.sendError(resp, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void init(){
        this.exchangeService = new ExchangeService();
        this.validator = new Validator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if(pathInfo != null) {
                throw new BadRequestException(INCORRECT_PATH_VARIABLES.getMessage());
            }
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            String amount = req.getParameter("amount");

            validator.validateParameter(from, "from");
            validator.validateParameter(to, "to");
            validator.validateParameter(amount, "amount");

            ExchangePairDto exPairDto = new ExchangePairDto();
            exPairDto.setBaseCurrencyCode(from);
            exPairDto.setTargetCurrencyCode(to);
            exPairDto.setAmount(amount);

            ExchangeDto exDto = exchangeService.get(exPairDto);
            ResponseUtils.sendJson(res, exDto, SC_OK);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

}