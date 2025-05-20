package org.example.service;

import org.example.dao.CurrenciesDao;
import org.example.dto.CurrencyDto;
import org.example.entity.Currency;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.DataBaseException;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import static org.example.handler.ErrorMessages.*;

public class CurrenciesService implements ServiceCurrencies {

    private final CurrenciesDao currenciesDao;
    private final Validator validator;

    public CurrenciesService()  {
        this.currenciesDao = new CurrenciesDao();
        this.validator = new Validator();
    }

    public CurrencyDto getByCode(String code) {
        try {
            validator.validateCode(code);
            Currency currency = currenciesDao.getByCode(code);
            return toCurrencyDto(currency);
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public CurrencyDto get(Integer id) {
        try {
            Currency currency = currenciesDao.getById(id);
            return toCurrencyDto(currency);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
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
            validator.validateName(currencyDto.getName());
            validator.validateCode(currencyDto.getCode());
            validator.validateSign(currencyDto.getSign());

//            String code = currencyDto.getCode();
//            if(currenciesDao.isExist(code)){
//                throw new ExistInDbException(EXIST_CURRENCY.getMessage() + code);
//            }

            Currency currency = toCurrency(currencyDto);
            currency = currenciesDao.save(currency);

            return toCurrencyDto(currency);
        } catch (ExistInDbException e) {
            throw new ExistInDbException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
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
