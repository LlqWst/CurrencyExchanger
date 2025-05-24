package dev.lqwd.controller;

import java.io.*;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dev.lqwd.dto.CurrencyDto;
import dev.lqwd.exceptions.CurrenciesExceptions;
import dev.lqwd.exceptions.custom_exceptions.BadRequestException;
import dev.lqwd.response_utils.ResponseUtils;
import dev.lqwd.service.CurrenciesService;
import dev.lqwd.validator.Validator;

import static jakarta.servlet.http.HttpServletResponse.*;
import static dev.lqwd.exceptions.ErrorMessages.*;

@WebServlet("/currencies/*")
public class CurrenciesController extends HttpServlet{

    private CurrenciesService currenciesService;
    private Validator validator;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            if ("GET".equalsIgnoreCase(req.getMethod())) {
                doGet(req, res);
            } else if ("POST".equalsIgnoreCase(req.getMethod())) {
                doPost(req, res);
            } else {
                ResponseUtils.sendError(res, INCORRECT_METHOD.getMessage(), SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e){
            ResponseUtils.sendError(res, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void init(){
        this.currenciesService = new CurrenciesService();
        this.validator = new Validator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if(pathInfo == null) {
                List<CurrencyDto> currenciesDto = currenciesService.getAll();
                ResponseUtils.sendJson(res, currenciesDto, SC_OK);
                return;
            }
            if (pathInfo.equals("/")) {
                throw new BadRequestException(MISSING_CURRENCY.getMessage());
            }

            String code = pathInfo.split("/")[1];
            validator.validateCode(code);
            CurrencyDto currencyDto = currenciesService.getByCode(code);
            ResponseUtils.sendJson(res, currencyDto, SC_OK);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            validator.validatePathVariable(pathInfo);
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            validator.validateParameter(code);
            validator.validateParameter(name);
            validator.validateParameter(sign);
            validator.validateCreation(code, name, sign);

            CurrencyDto dto = new CurrencyDto();
            dto.setName(name);
            dto.setCode(code);
            dto.setSign(sign);

            CurrencyDto currencyDto = currenciesService.save(dto);
            ResponseUtils.sendJson(res, currencyDto, SC_CREATED);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

}