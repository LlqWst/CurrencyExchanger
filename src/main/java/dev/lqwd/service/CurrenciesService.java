package dev.lqwd.service;

import dev.lqwd.dao.CurrenciesDao;
import dev.lqwd.dto.CurrencyDto;
import dev.lqwd.mapper.CurrencyAndRateMapper;
import dev.lqwd.entity.Currency;
import dev.lqwd.exceptions.CurrenciesExceptions;
import dev.lqwd.exceptions.custom_exceptions.DataBaseException;

import java.util.ArrayList;
import java.util.List;

import static dev.lqwd.exceptions.ErrorMessages.*;

public class CurrenciesService {

    private final CurrenciesDao currenciesDao;
    private final CurrencyAndRateMapper currencyMapper;

    public CurrenciesService()  {
        this.currenciesDao = new CurrenciesDao();
        this.currencyMapper = CurrencyAndRateMapper.INSTANCE;
    }

    public CurrencyDto getByCode(String code) {
        try {
            Currency currencyDao = currenciesDao.getByCode(code);
            return currencyMapper.toCurrencyDto(currencyDao);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public CurrencyDto get(Integer id) {
        try {
            Currency currencyDao = currenciesDao.getById(id);
            return currencyMapper.toCurrencyDto(currencyDao);
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
                CurrencyDto currencyDto = currencyMapper.toCurrencyDto(currency);
                currenciesDto.add(currencyDto);
            }
            return currenciesDto;
        } catch (Exception e) {
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public CurrencyDto save(CurrencyDto currencyDto){
        try {
            Currency currency = currencyMapper.toCurrency(currencyDto);
            Currency currencyDao = currenciesDao.save(currency);
            return currencyMapper.toCurrencyDto(currencyDao);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

}
