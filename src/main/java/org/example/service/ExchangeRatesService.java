package org.example.service;

import org.example.dao.ExchangeRatesDao;
import org.example.dto.CurrencyDto;
import org.example.dto.ExchangePairDto;
import org.example.dto.ExchangeRateDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.DataBaseException;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.validation.Validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.example.handler.ErrorMessages.EXIST_PAIR;
import static org.example.handler.ErrorMessages.INTERNAL_ERROR;

public class ExchangeRatesService {

    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrenciesService currenciesService;
    private final Validator validator;

    public ExchangeRatesService() {
        this.exchangeRatesDao = new ExchangeRatesDao();
        this.currenciesService = new CurrenciesService();
        this.validator = new Validator();
    }

        public ExchangeRateDto get(String exPair) {
        try {
            validator.validatePair(exPair);
            String baseCode = exPair.substring(0, 3);
            String targetCode = exPair.substring(3, 6);

            CurrencyDto baseCurrencyDto = currenciesService.getByCode(baseCode);
            CurrencyDto targetCurrencyDto = currenciesService.getByCode(targetCode);

            Currency baseCurrency = currenciesService.toCurrency(baseCurrencyDto);
            Currency targetCurrency = currenciesService.toCurrency(targetCurrencyDto);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            exRate = exchangeRatesDao.get(exRate);

            return toExchangeRateDto(exRate);
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
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

            validator.validateCode(baseCode);
            validator.validateCode(targetCode);

            BigDecimal rate = validator.parsRate(exPairDto.getRate());

            CurrencyDto baseCurrencyDto = currenciesService.getByCode(baseCode);
            CurrencyDto targetCurrencyDto = currenciesService.getByCode(targetCode);

            Currency baseCurrency = currenciesService.toCurrency(baseCurrencyDto);
            Currency targetCurrency = currenciesService.toCurrency(targetCurrencyDto);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            exRate.setRate(rate);
            if(exchangeRatesDao.isExist(exRate)){
                throw new ExistInDbException(EXIST_PAIR.getMessage());
            }
            exRate = exchangeRatesDao.save(exRate);

            return toExchangeRateDto(exRate);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ExistInDbException e) {
            throw new ExistInDbException(e.getMessage());
        } catch (Exception e) {
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    public ExchangeRateDto update (ExchangePairDto exPairDto) {
        try {
            String pair = exPairDto.getPair();
            validator.validatePair(pair);
            String baseCode = pair.substring(0, 3);
            String targetCode = pair.substring(3, 6);

            String encryptedRate = exPairDto.getRate();
            String decryptedRate = validator.patchParsRate(encryptedRate);
            BigDecimal rate = validator.parsRate(decryptedRate);

            CurrencyDto baseCurrencyDto = currenciesService.getByCode(baseCode);
            CurrencyDto targetCurrencyDto = currenciesService.getByCode(targetCode);

            Currency baseCurrency = currenciesService.toCurrency(baseCurrencyDto);
            Currency targetCurrency = currenciesService.toCurrency(targetCurrencyDto);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            exRate.setRate(rate);
            exRate = exchangeRatesDao.update(exRate);

            return toExchangeRateDto(exRate);
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
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
