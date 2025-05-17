package org.example.service;

import org.example.dao.CurrenciesDao;
import org.example.dao.ExchangeRatesDao;
import org.example.dto.CurrencyDto;
import org.example.dto.ExchangePairDto;
import org.example.dto.ExchangeRateDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesService {

    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrenciesDao currenciesDao;

    public ExchangeRatesService() {
        this.exchangeRatesDao = new ExchangeRatesDao();
        this.currenciesDao = new CurrenciesDao();
    }

        public ExchangeRateDto get(ExchangePairDto exPairDto) {
        try {
            CurrencyDto baseCurrencyDto = new CurrencyDto();
            CurrencyDto targetCurrencyDto = new CurrencyDto();
            baseCurrencyDto.setCode(exPairDto.getBaseCurrency());
            targetCurrencyDto.setCode(exPairDto.getTargetCurrency());

            Currency baseCurrency = currenciesDao.getByCode(baseCurrencyDto);
            Currency targetCurrency = currenciesDao.getByCode(targetCurrencyDto);

            ExchangeRateDto exRateDto = new ExchangeRateDto();
            exRateDto.setBaseCurrency(baseCurrency);
            exRateDto.setTargetCurrency(targetCurrency);
            ExchangeRate exRate = exchangeRatesDao.get(exRateDto);
            exRateDto.setId(exRate.getId());
            exRateDto.setRate(exRate.getRate());
            return exRateDto;
        } catch (BadRequestException e) {
            throw new BadRequestException ();
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    public List<ExchangeRateDto> getAll() {
        try {
            List<ExchangeRate> exRates = exchangeRatesDao.getAll();
            List<ExchangeRateDto> exRatesDto = new ArrayList<>();
            for (ExchangeRate exRate : exRates){
                CurrencyDto baseCurrencyDto = new CurrencyDto();
                CurrencyDto targetCurrencyDto = new CurrencyDto();
                baseCurrencyDto.setId(exRate.getBaseCurrency().getId());
                targetCurrencyDto.setId(exRate.getTargetCurrency().getId());

                ExchangeRateDto exRateDto = new ExchangeRateDto();
                exRateDto.setId(exRate.getId());
                exRateDto.setBaseCurrency(currenciesDao.getById(baseCurrencyDto));
                exRateDto.setTargetCurrency(currenciesDao.getById(targetCurrencyDto));
                exRateDto.setRate(exRate.getRate());
                exRatesDto.add(exRateDto);
            }
            return exRatesDto;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public ExchangeRateDto save(ExchangePairDto exPairDto){
        try {
            CurrencyDto baseCurrencyDto = new CurrencyDto();
            CurrencyDto targetCurrencyDto = new CurrencyDto();
            baseCurrencyDto.setCode(exPairDto.getBaseCurrency());
            targetCurrencyDto.setCode(exPairDto.getTargetCurrency());

            Currency baseCurrency = currenciesDao.getByCode(baseCurrencyDto);
            Currency targetCurrency = currenciesDao.getByCode(targetCurrencyDto);

            ExchangeRateDto exRateDto = new ExchangeRateDto();
            exRateDto.setBaseCurrency(baseCurrency);
            exRateDto.setTargetCurrency(targetCurrency);
            exRateDto.setRate(exPairDto.getRate());
            ExchangeRate exRate = exchangeRatesDao.save(exRateDto);
            exRateDto.setId(exRate.getId());

            return exRateDto;
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ExistInDbException e) {
            throw new ExistInDbException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
