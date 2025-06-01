package dev.lqwd.service;

import dev.lqwd.dao.CurrencyDao;
import dev.lqwd.dto.CurrencyResponseDto;
import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.exceptions.NotFoundException;
import dev.lqwd.mapper.CurrencyMapper;
import dev.lqwd.entity.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesService {

    private final static String NOT_EXIST_CURRENCY_CODE = "Not found currency with code %s";
    private final static String NOT_EXIST_CURRENCY_ID = "Not found currency with ID %s";
    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrenciesService() {
        this.currencyDao = new CurrencyDao();
        this.currencyMapper = CurrencyMapper.INSTANCE;
    }

    public CurrencyResponseDto getByCode(String code) {
        Currency currencyDao = this.currencyDao.getByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_EXIST_CURRENCY_CODE, code)));

        return currencyMapper.toResponseDto(currencyDao);
    }

    public CurrencyResponseDto get(Long id) {
        Currency currencyDao = this.currencyDao.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_EXIST_CURRENCY_ID, id)));

        return currencyMapper.toResponseDto(currencyDao);
    }

    public List<CurrencyResponseDto> getAll() {
        List<CurrencyResponseDto> currenciesDto = new ArrayList<>();
        List<Currency> currencies = currencyDao.getAll();

        for (Currency currency : currencies) {
            CurrencyResponseDto currencyResponseDto = currencyMapper.toResponseDto(currency);
            currenciesDto.add(currencyResponseDto);
        }

        return currenciesDto;
    }

    public CurrencyResponseDto save(CurrencyRequestDto currencyRequestDto) {
        Currency currency = currencyMapper.toCurrency(currencyRequestDto);
        Currency currencyDao = this.currencyDao.save(currency);

        return currencyMapper.toResponseDto(currencyDao);
    }

}
