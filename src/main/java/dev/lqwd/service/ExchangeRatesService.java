package dev.lqwd.service;

import dev.lqwd.dao.CurrencyDao;
import dev.lqwd.dao.ExchangeRateDao;
import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.exception.NotFoundException;
import dev.lqwd.dto.ExchangeRateRequestDto;
import dev.lqwd.entity.Currency;
import dev.lqwd.entity.ExchangeRate;
import dev.lqwd.mapper.ExchangeRateMapper;

import java.util.List;

public class ExchangeRatesService {

    private final static String NO_CURRENCY_CODE_EXIST = "Not found currency with code %s";
    private final static String NOT_FOUND_PAIR = "The exchange rate for the pair was not found";
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyDao currencyDao;
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    public ExchangeRatesService() {

        this.exchangeRateDao = new ExchangeRateDao();
        this.currencyDao = new CurrencyDao();
    }

    public ExchangeRateResponseDto getByPair(String from, String to) {

        ExchangeRate exRateEntity = exchangeRateDao.findByPair(from, to)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PAIR));

        return mapper.toExchangeResponseDto(exRateEntity);
    }

    public List<ExchangeRateResponseDto> getAll() {

        List<ExchangeRate> exRates = exchangeRateDao.findAll();

        return exRates.stream()
                .map(mapper::toExchangeResponseDto)
                .toList();
    }

    public ExchangeRateResponseDto save(ExchangeRateRequestDto exRateRequestDto) {

        ExchangeRate exRate = getExchangeRate(exRateRequestDto);

        ExchangeRate SavedExRate = exchangeRateDao.save(exRate);

        return mapper.toExchangeResponseDto(SavedExRate);
    }

    public ExchangeRateResponseDto update(ExchangeRateRequestDto exRateRequestDto) {

        ExchangeRate exRate = getExchangeRate(exRateRequestDto);

        ExchangeRate exRateEntity = exchangeRateDao.update(exRate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PAIR));

        return mapper.toExchangeResponseDto(exRateEntity);
    }

    private ExchangeRate getExchangeRate(ExchangeRateRequestDto exRateRequestDto) {

        return new ExchangeRate(
                codeToCurrency(exRateRequestDto.getBaseCurrencyCode()),
                codeToCurrency(exRateRequestDto.getTargetCurrencyCode()),
                exRateRequestDto.getRate()
        );
    }

    private Currency codeToCurrency(String code) {

        return currencyDao.findByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format(NO_CURRENCY_CODE_EXIST, code)));
    }

}
