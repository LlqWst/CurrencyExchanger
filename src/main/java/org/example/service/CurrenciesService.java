package org.example.service;

import org.example.dao.CurrenciesDao;
import org.example.dto.CurrencyDto;
import org.example.entity.Currency;
import org.example.exceptions.CurrenciesExceptions;
import org.example.exceptions.custom_exceptions.DataBaseException;

import java.util.ArrayList;
import java.util.List;

import static org.example.exceptions.ErrorMessages.*;

public class CurrenciesService {

    private final CurrenciesDao currenciesDao;

    public CurrenciesService()  {
        this.currenciesDao = new CurrenciesDao();
    }

    public CurrencyDto getByCode(String code) {
        try {
            Currency currency = currenciesDao.getByCode(code);
            return toCurrencyDto(currency);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public CurrencyDto get(Integer id) {
        try {
            Currency currency = currenciesDao.getById(id);
            return toCurrencyDto(currency);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public List<CurrencyDto> getAll() {
        try {
            List<CurrencyDto> currenciesDto = new ArrayList<>();
            List<Currency> currencies = currenciesDao.getAll();
            for(Currency currency : currencies){
                CurrencyDto currencyDto = toCurrencyDto(currency);
                currenciesDto.add(currencyDto);
            }
            return currenciesDto;
        } catch (Exception e) {
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public CurrencyDto save(CurrencyDto currencyDto){
        try {
            Currency currency = toCurrency(currencyDto);
            currency = currenciesDao.save(currency);
            return toCurrencyDto(currency);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public CurrencyDto toCurrencyDto (Currency currency){
        CurrencyDto dto = new CurrencyDto();
        dto.setId(currency.getId());
        dto.setName(currency.getName());
        dto.setCode(currency.getCode());
        dto.setSign(currency.getSign());
        return dto;
    }

    public Currency toCurrency (CurrencyDto dto){
        Currency currency = new Currency();
        currency.setId(dto.getId());
        currency.setName(dto.getName());
        currency.setCode(dto.getCode());
        currency.setSign(dto.getSign());
        return currency;
    }

}
