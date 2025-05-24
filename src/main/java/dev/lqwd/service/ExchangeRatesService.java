package dev.lqwd.service;

import dev.lqwd.dao.ExchangeRatesDao;
import dev.lqwd.dto.CurrencyDto;
import dev.lqwd.mapper.CurrencyAndRateMapper;
import dev.lqwd.dto.ExchangePairDto;
import dev.lqwd.dto.ExchangeRateDto;
import dev.lqwd.entity.Currency;
import dev.lqwd.entity.ExchangeRate;
import dev.lqwd.exceptions.CurrenciesExceptions;
import dev.lqwd.exceptions.custom_exceptions.DataBaseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static dev.lqwd.exceptions.ErrorMessages.INTERNAL_ERROR;

public class ExchangeRatesService {

    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrenciesService currenciesService;
    private final CurrencyAndRateMapper currencyMapper;

    public ExchangeRatesService() {
        this.exchangeRatesDao = new ExchangeRatesDao();
        this.currenciesService = new CurrenciesService();
        this.currencyMapper = CurrencyAndRateMapper.INSTANCE;
    }

    public ExchangeRateDto get(String exPair) {
        try {
            String baseCode = exPair.substring(0, 3);
            String targetCode = exPair.substring(3, 6);

            Currency baseCurrency = codeToCurrency(baseCode);
            Currency targetCurrency = codeToCurrency(targetCode);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            ExchangeRate exRateEntity = exchangeRatesDao.get(exRate);

            return currencyMapper.toExchangeRateDto(exRateEntity);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public List<ExchangeRateDto> getAll() {
        try {
            List<ExchangeRate> exRates = exchangeRatesDao.getAll();
            List<ExchangeRateDto> exRatesDto = new ArrayList<>();
            for (ExchangeRate exRate : exRates){
                int baseCode = exRate.getBaseCurrency().getId();
                int targetCode = exRate.getTargetCurrency().getId();

                CurrencyDto baseCurrencyDto = currenciesService.get(baseCode);
                CurrencyDto targetCurrencyDto = currenciesService.get(targetCode);

                ExchangeRateDto exRateDto = new ExchangeRateDto();
                exRateDto.setBaseCurrency(baseCurrencyDto);
                exRateDto.setTargetCurrency(targetCurrencyDto);
                exRateDto.setId(exRate.getId());
                exRateDto.setRate(exRate.getRate());

                exRatesDto.add(exRateDto);
            }
            return exRatesDto;
        } catch (Exception e) {
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public ExchangeRateDto save(ExchangePairDto exPairDto){
        try {
            ExchangeRate exRate = setExPair(exPairDto);
            ExchangeRate exRateEntity = exchangeRatesDao.save(exRate);
            return currencyMapper.toExchangeRateDto(exRateEntity);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public ExchangeRateDto update (ExchangePairDto exPairDto) {
        try {
            ExchangeRate exRate = setExPair(exPairDto);
            ExchangeRate exRateEntity = exchangeRatesDao.update(exRate);
            return currencyMapper.toExchangeRateDto(exRateEntity);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    private ExchangeRate setExPair(ExchangePairDto exPairDto) {
        String baseCode = exPairDto.getBaseCurrencyCode();
        String targetCode = exPairDto.getTargetCurrencyCode();
        BigDecimal rate = exPairDto.getRate();

        Currency baseCurrency = codeToCurrency(baseCode);
        Currency targetCurrency = codeToCurrency(targetCode);

        ExchangeRate exRate = new ExchangeRate();
        exRate.setBaseCurrency(baseCurrency);
        exRate.setTargetCurrency(targetCurrency);
        exRate.setRate(rate);
        return exRate;
    }

    private Currency codeToCurrency(String code){
        CurrencyDto currencyDto = currenciesService.getByCode(code);
        return currencyMapper.toCurrency(currencyDto);
    }

}
