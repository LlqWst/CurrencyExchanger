package dev.lqwd.service;

import dev.lqwd.dao.CurrencyDao;
import dev.lqwd.dao.ExchangeRateDao;
import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.exceptions.NotFoundException;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.entity.Currency;
import dev.lqwd.entity.ExchangeRate;
import dev.lqwd.mapper.ExchangeRateMapper;

import java.util.ArrayList;
import java.util.List;

import static dev.lqwd.exceptions.ErrorMessages.NOT_EXIST_CURRENCY;
import static dev.lqwd.exceptions.ErrorMessages.NOT_FOUND_PAIR;

public class ExchangeRatesService {

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
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PAIR.getMessage()));

        return exchangeRateMapper.toExchangeRateResponseDto(exRateEntity);
    }

    public List<ExchangeRateResponseDto> getAll() {

        List<ExchangeRate> exRates = exchangeRateDao.getAll();
        List<ExchangeRateResponseDto> exRatesDto = new ArrayList<>();

        for (ExchangeRate exRate : exRates){
            exRatesDto.add(exchangeRateMapper.toExchangeRateResponseDto(exRate));
        }

        return exRatesDto;
    }

    public ExchangeRateResponseDto save(ExchangeRateRequestDto requestDto){
        ExchangeRate exRateToDao = getExchangeRate(requestDto);

        ExchangeRate exRateFromDao = exchangeRateDao.save(exRateToDao);

        return exchangeRateMapper.toExchangeRateResponseDto(exRateFromDao);
    }

    public ExchangeRateResponseDto update (ExchangeRateRequestDto requestDto) {
        ExchangeRate exRate = getExchangeRate(requestDto);

        ExchangeRate exRateEntity = exchangeRateDao.update(exRate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PAIR.getMessage()));

        return exchangeRateMapper.toExchangeRateResponseDto(exRateEntity);
    }

    private ExchangeRate getExchangeRate(ExchangeRateRequestDto requestDto) {
        return new ExchangeRate(
                codeToCurrency(requestDto.getBaseCurrencyCode()),
                codeToCurrency(requestDto.getTargetCurrencyCode()),
                requestDto.getRate()
        );
    }

    private Currency codeToCurrency(String code){
        return currencyDao.getByCode(code)
                .orElseThrow(() -> new NotFoundException(NOT_EXIST_CURRENCY.getMessage() + code));
    }

}
