package org.example.controller;

import java.io.*;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.dto.CurrencyDto;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.DataBaseException;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.controller.response_utils.ResponseUtils;
import org.example.service.CurrenciesService;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.example.handler.ErrorMessages.*;

@WebServlet("/currencies/*")
public class CurrenciesController extends HttpServlet{

    private CurrenciesService currenciesService;

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
        this.currenciesService = new CurrenciesService();
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
            CurrencyDto currencyDto = currenciesService.getByCode(code);
            ResponseUtils.sendJson(res, currencyDto, SC_OK);
        } catch (BadRequestException e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        } catch (NotFoundException e){
            ResponseUtils.sendError(res, NOT_EXIST_CURRENCY.getMessage(), e.getStatusCode());
        } catch (DataBaseException e) {
            ResponseUtils.sendError(res, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
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
            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setName(name);
            currencyDto.setCode(code);
            currencyDto.setSign(sign);
            currencyDto = currenciesService.save(currencyDto);
            ResponseUtils.sendJson(res, currencyDto, SC_ACCEPTED);
        } catch (BadRequestException e) {
            ResponseUtils.sendError(res, e.getMessage(), e.getStatusCode());
        } catch (ExistInDbException e){
            ResponseUtils.sendError(res, EXIST_CURRENCY.getMessage(), e.getStatusCode());
        } catch (DataBaseException e){
            ResponseUtils.sendError(res, INTERNAL_ERROR.getMessage(), SC_INTERNAL_SERVER_ERROR);
        }
    }

}