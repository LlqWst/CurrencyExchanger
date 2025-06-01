package dev.lqwd.service;

import dev.lqwd.dao.CurrencyDao;
import dev.lqwd.dao.ExchangeRateDao;
import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.exception.NotFoundException;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.entity.Currency;
import dev.lqwd.entity.ExchangeRate;
import dev.lqwd.mapper.ExchangeRateMapper;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesService {

    private final static String NO_CURRENCY_CODE_EXIST = "Not found currency with code %s";
    private final static String NOT_FOUND_PAIR = "The exchange rate for the pair was not found";
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyDao currencyDao;
    private final ExchangeRateMapper exchangeRateMapper;

    public ExchangeRatesService() {
        this.exchangeRateDao = new ExchangeRateDao();
        this.exchangeRateMapper = ExchangeRateMapper.INSTANCE;
        this.currencyDao = new CurrencyDao();
    }

    public ExchangeRateResponseDto getByPair(String from, String to) {

        ExchangeRate exRateEntity = exchangeRateDao.getByPair(from, to)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PAIR));

        return exchangeRateMapper.toResponseDto(exRateEntity);
    }

    public List<ExchangeRateResponseDto> getAll() {

        List<ExchangeRate> exRates = exchangeRateDao.getAll();
        List<ExchangeRateResponseDto> exRatesDto = new ArrayList<>();

        for (ExchangeRate exRate : exRates) {
            exRatesDto.add(exchangeRateMapper.toResponseDto(exRate));
        }

        return exRatesDto;
    }

    public ExchangeRateResponseDto save(ExchangeRateRequestDto requestDto) {
        ExchangeRate exRateToDao = getExchangeRate(requestDto);

        ExchangeRate exRateFromDao = exchangeRateDao.save(exRateToDao);

        return exchangeRateMapper.toResponseDto(exRateFromDao);
    }

    public ExchangeRateResponseDto update(ExchangeRateRequestDto requestDto) {
        ExchangeRate exRate = getExchangeRate(requestDto);

        ExchangeRate exRateEntity = exchangeRateDao.update(exRate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PAIR));

        return exchangeRateMapper.toResponseDto(exRateEntity);
    }

    private ExchangeRate getExchangeRate(ExchangeRateRequestDto requestDto) {
        return new ExchangeRate(
                codeToCurrency(requestDto.getBaseCurrencyCode()),
                codeToCurrency(requestDto.getTargetCurrencyCode()),
                requestDto.getRate()
        );
    }

    private Currency codeToCurrency(String code) {
        return currencyDao.getByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format(NO_CURRENCY_CODE_EXIST, code)));
    }

}
