package dev.lqwd.service;

import dev.lqwd.dao.CurrencyDao;
import dev.lqwd.dto.CurrencyResponseDto;
import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.exception.NotFoundException;
import dev.lqwd.mapper.CurrencyMapper;
import dev.lqwd.entity.Currency;

import java.util.List;

public class CurrenciesService {

    private final static String NOT_EXIST_CURRENCY_CODE = "Not found currency with code %s";
    private final static String NOT_EXIST_CURRENCY_ID = "Not found currency with ID %s";

    private final CurrencyDao currencyDao;
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    public CurrenciesService() {

        this.currencyDao = new CurrencyDao();
    }

    public CurrencyResponseDto getByCode(String code) {

        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_EXIST_CURRENCY_CODE, code)));

        return mapper.toCurrencyResponseDto(currency);
    }

    public CurrencyResponseDto get(Long id) {

        Currency currency = currencyDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_EXIST_CURRENCY_ID, id)));

        return mapper.toCurrencyResponseDto(currency);
    }

    public List<CurrencyResponseDto> getAll() {

        List<Currency> currencies = currencyDao.findAll();

        return currencies.stream()
                .map(mapper::toCurrencyResponseDto)
                .toList();
    }

    public CurrencyResponseDto save(CurrencyRequestDto currencyRequestDto) {

        Currency currency = mapper.toCurrency(currencyRequestDto);
        Currency savedCurrency = currencyDao.save(currency);

        return mapper.toCurrencyResponseDto(savedCurrency);
    }

}
