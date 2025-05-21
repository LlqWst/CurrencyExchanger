package org.example.service;

import org.example.dao.ExchangeRatesDao;
import org.example.dto.CurrencyDto;
import org.example.dto.ExchangePairDto;
import org.example.dto.ExchangeRateDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.exceptions.CurrenciesExceptions;
import org.example.exceptions.custom_exceptions.BadRequestException;
import org.example.exceptions.custom_exceptions.DataBaseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.example.exceptions.ErrorMessages.INTERNAL_ERROR;

public class ExchangeRatesService {

    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrenciesService currenciesService;

    public ExchangeRatesService() {
        this.exchangeRatesDao = new ExchangeRatesDao();
        this.currenciesService = new CurrenciesService();
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
            exRate = exchangeRatesDao.get(exRate);

            return toExchangeRateDto(exRate);
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
                exRateDto.setBaseCurrencyDto(baseCurrencyDto);
                exRateDto.setTargetCurrencyDto(targetCurrencyDto);
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
            String baseCode = exPairDto.getBaseCurrencyCode();
            String targetCode = exPairDto.getTargetCurrencyCode();
            BigDecimal rate = exPairDto.getRate();

            Currency baseCurrency = codeToCurrency(baseCode);
            Currency targetCurrency = codeToCurrency(targetCode);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            exRate.setRate(rate);
            exRate = exchangeRatesDao.save(exRate);

            return toExchangeRateDto(exRate);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public ExchangeRateDto update (ExchangePairDto exPairDto) {
        try {
            String pair = exPairDto.getPair();
            String baseCode = pair.substring(0, 3);
            String targetCode = pair.substring(3, 6);
            BigDecimal rate = exPairDto.getRate();

            Currency baseCurrency = codeToCurrency(baseCode);
            Currency targetCurrency = codeToCurrency(targetCode);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            exRate.setRate(rate);
            exRate = exchangeRatesDao.update(exRate);

            return toExchangeRateDto(exRate);
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    private Currency codeToCurrency(String code){
        CurrencyDto currencyDto = currenciesService.getByCode(code);
        return currenciesService.toCurrency(currencyDto);
    }

    private ExchangeRateDto toExchangeRateDto (ExchangeRate exRate){
        int id = exRate.getId();
        BigDecimal rate = exRate.getRate();
        CurrencyDto baseCurrencyDto = currenciesService.toCurrencyDto(exRate.getBaseCurrency());
        CurrencyDto targetCurrencyDto = currenciesService.toCurrencyDto(exRate.getTargetCurrency());

        ExchangeRateDto exRateDto = new ExchangeRateDto();
        exRateDto.setBaseCurrencyDto(baseCurrencyDto);
        exRateDto.setTargetCurrencyDto(targetCurrencyDto);
        exRateDto.setId(id);
        exRateDto.setRate(rate);
        return exRateDto;
    }

}
