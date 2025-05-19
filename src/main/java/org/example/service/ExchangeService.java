package org.example.service;

import org.example.dto.CurrencyDto;
import org.example.dto.ExchangeDto;
import org.example.dto.ExchangePairDto;
import org.example.dto.ExchangeRateDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.DataBaseException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.validation.Validator;

import java.math.BigDecimal;

import static org.example.handler.ErrorMessages.INTERNAL_ERROR;

public class ExchangeService {

    private final static String USD_CODE = "USD";

    private final ExchangeRatesService exchangeRatesService;
    private final Validator validator;

    public ExchangeService() {
        this.exchangeRatesService = new ExchangeRatesService();
        this.validator = new Validator();
    }

    public ExchangeDto get(ExchangePairDto exPairDto) {
        try {
            String baseCode = exPairDto.getBaseCurrencyCode();
            String targetCode = exPairDto.getTargetCurrencyCode();

            String pair1 = baseCode + targetCode;
            String pair2 = targetCode + baseCode;
            String pair3 = USD_CODE + baseCode;
            String pair4 = USD_CODE + targetCode;

            ExchangeRateDto exRateDto = exchangeRatesService.get(pair1);
            exchangeRatesService.get(pair2);
            exchangeRatesService.get(pair3);
            exchangeRatesService.get(pair4);

            String amountDto = exPairDto.getAmount();
            BigDecimal amount = validator.parsAmount(amountDto);
            BigDecimal rate = exRateDto.rate();
            BigDecimal convertedAmount = amount.multiply(rate);

            return new ExchangeDto(exRateDto.baseCurrencyDto(), exRateDto.targetCurrencyDto(), rate, amount, convertedAmount);
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }
}
