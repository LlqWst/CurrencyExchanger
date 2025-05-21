package org.example.controller;

import java.io.*;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.dto.CurrencyDto;
import org.example.exceptions.CurrenciesExceptions;
import org.example.exceptions.custom_exceptions.BadRequestException;
import org.example.controller.response_utils.ResponseUtils;
import org.example.service.CurrenciesService;
import org.example.validation.Validator;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.example.exceptions.ErrorMessages.*;

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
            if(pathInfo != null){
                throw new BadRequestException(INCORRECT_PATH_VARIABLES.getMessage());
            }
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            validation(name, code, sign);

            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setName(name);
            currencyDto.setCode(code);
            currencyDto.setSign(sign);
            currencyDto = currenciesService.save(currencyDto);
            ResponseUtils.sendJson(res, currencyDto, SC_CREATED);
        } catch (CurrenciesExceptions e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        }
    }

    private void validation(String name, String code, String sign){
        validator.validateParameter(name, "name");
        validator.validateName(name);

        validator.validateParameter(code, "code");
        validator.validateCode(code);

        validator.validateParameter(sign, "sign");
        validator.validateSign(sign);
    }

}