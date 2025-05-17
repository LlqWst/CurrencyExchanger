package org.example.service;

import org.example.dao.CurrenciesDao;
import org.example.dto.CurrencyDto;
import org.example.entity.Currency;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesService {

    private final CurrenciesDao dao;

    public CurrenciesService() {
        this.dao = new CurrenciesDao();
    }

    public CurrencyDto get(CurrencyDto currencyDto) {
        try {
            Currency currency = dao.getByCode(currencyDto);
            currencyDto.setId(currency.getId());
            currencyDto.setName(currency.getName());
            currencyDto.setSign(currency.getSign());
            return currencyDto;
        } catch (BadRequestException e) {
            throw new BadRequestException ();
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    public List<CurrencyDto> getAll() {
        try {
            List<CurrencyDto> currenciesDto = new ArrayList<>();
            List<Currency> currencies = dao.getAll();
            for(Currency currency : currencies){
                CurrencyDto currencyDto = new CurrencyDto();
                currencyDto.setId(currency.getId());
                currencyDto.setCode(currency.getCode());
                currencyDto.setName(currency.getName());
                currencyDto.setSign(currency.getSign());
                currenciesDto.add(currencyDto);
            }
            return currenciesDto;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public CurrencyDto save(CurrencyDto currencyDto){
        try {
            Currency currency = dao.save(currencyDto);
            currencyDto.setId(currency.getId());
            return currencyDto;
        } catch (ExistInDbException e){
          throw new ExistInDbException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
