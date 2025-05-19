package org.example.service;

import org.example.dto.CurrencyDto;
import org.example.dto.ExchangeDto;
import org.example.dto.ExchangePairDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.DataBaseException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.validation.Validator;

import java.math.BigDecimal;

import static org.example.handler.ErrorMessages.INTERNAL_ERROR;

public class ExchangeService {

    private final ExchangeRatesService exchangeRatesService;
    private final CurrenciesService currenciesService;
    private final Validator validator;

    public ExchangeService() {
        this.exchangeRatesService = new ExchangeRatesService();
        this.currenciesService = new CurrenciesService();
        this.validator = new Validator();
    }

    public ExchangeDto get(ExchangePairDto exPairDto) {
        try {
            String baseCode = exPairDto.getBaseCurrencyCode();
            String targetCode = exPairDto.getTargetCurrencyCode();
            String amountDto = exPairDto.getAmount();
            validator.validateCode(baseCode);
            validator.validateCode(targetCode);
            BigDecimal amount = validator.parsAmount(amountDto);

            CurrencyDto baseCurrencyDto = currenciesService.getByCode(baseCode);
            CurrencyDto targetCurrencyDto = currenciesService.getByCode(targetCode);

            Currency baseCurrency = currenciesService.toCurrency(baseCurrencyDto);
            Currency targetCurrency = currenciesService.toCurrency(targetCurrencyDto);

            ExchangeRate exRate = new ExchangeRate();
            exRate.setBaseCurrency(baseCurrency);
            exRate.setTargetCurrency(targetCurrency);
            //exRate = exchangeRatesDao.get(exRate);
            //return toExchangeRateDto(exRate);
            return new ExchangeDto(baseCurrencyDto, targetCurrencyDto, amount ,amount, amount);
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }
}
