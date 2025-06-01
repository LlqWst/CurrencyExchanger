package dev.lqwd.service;

import dev.lqwd.dao.CurrencyDao;
import dev.lqwd.dto.CurrencyResponseDto;
import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.exceptions.NotFoundException;
import dev.lqwd.mapper.CurrencyMapper;
import dev.lqwd.entity.Currency;

import java.util.ArrayList;
import java.util.List;

import static dev.lqwd.exceptions.ErrorMessages.NOT_EXIST_CURRENCY;


public class CurrenciesService {

    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrenciesService()  {
        this.currencyDao = new CurrencyDao();
        this.currencyMapper = CurrencyMapper.INSTANCE;
    }

    public CurrencyResponseDto getByCode(String code) {
        Currency currencyDao = this.currencyDao.getByCode(code)
                .orElseThrow(() -> new NotFoundException(NOT_EXIST_CURRENCY.getMessage() + code));

        return currencyMapper.toCurrencyResponseDto(currencyDao);
    }

    public CurrencyResponseDto get(Long id) {
        Currency currencyDao = this.currencyDao.getById(id)
                .orElseThrow(() -> new NotFoundException(NOT_EXIST_CURRENCY.getMessage() + id));

        return currencyMapper.toCurrencyResponseDto(currencyDao);
    }

    public List<CurrencyResponseDto> getAll() {
        List<CurrencyResponseDto> currenciesDto = new ArrayList<>();
        List<Currency> currencies = currencyDao.getAll();

        for(Currency currency : currencies){
            CurrencyResponseDto currencyResponseDto = currencyMapper.toCurrencyResponseDto(currency);
            currenciesDto.add(currencyResponseDto);
        }

        return currenciesDto;
    }

    public CurrencyResponseDto save(CurrencyRequestDto currencyRequestDto){
        Currency currency = currencyMapper.toCurrency(currencyRequestDto);
        Currency currencyDao = this.currencyDao.save(currency);

        return currencyMapper.toCurrencyResponseDto(currencyDao);
    }

}
