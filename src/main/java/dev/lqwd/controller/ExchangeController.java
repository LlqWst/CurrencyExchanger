package dev.lqwd.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.response_utils.ResponseUtils;
import dev.lqwd.dto.ExchangeDto;
import dev.lqwd.dto.ExchangePairDto;
import dev.lqwd.exceptions.CurrenciesExceptions;
import dev.lqwd.exceptions.custom_exceptions.BadRequestException;
import dev.lqwd.service.ExchangeService;
import dev.lqwd.validator.Validator;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.*;
import static dev.lqwd.exceptions.ErrorMessages.*;

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
            String encryptedAmount = req.getParameter("amount");

            validator.validateParameter(from, "from");
            validator.validateParameter(to, "to");
            validator.validatePair(from, to);

            validator.validateParameter(encryptedAmount, "amount");
            BigDecimal amount = validator.parsAmount(encryptedAmount);

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