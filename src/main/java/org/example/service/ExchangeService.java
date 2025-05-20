package org.example.service;

import org.example.dto.CurrencyDto;
import org.example.dto.ExchangeDto;
import org.example.dto.ExchangePairDto;
import org.example.handler.custom_exceptions.BadRequestException;
import org.example.handler.custom_exceptions.DataBaseException;
import org.example.handler.custom_exceptions.NotFoundException;
import org.example.validation.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.example.handler.ErrorMessages.INCORRECT_CONVERSION_INPUT;
import static org.example.handler.ErrorMessages.INTERNAL_ERROR;

public class ExchangeService {

    private final static String USD_CODE = "USD";
    private final static int SCALE = Validator.SCALE;

    private final ExchangeRatesService exchangeRatesService;
    private final CurrenciesService currenciesService;
    private final Validator validator;

    public ExchangeService() {
        this.exchangeRatesService = new ExchangeRatesService();
        this.validator = new Validator();
        this.currenciesService = new CurrenciesService();
    }

    public ExchangeDto get(ExchangePairDto exPairDto) {
        try {
            String baseCode = exPairDto.getBaseCurrencyCode();
            String targetCode = exPairDto.getTargetCurrencyCode();
            String amountDto = exPairDto.getAmount();

            CurrencyDto baseCurrencyDto = currenciesService.getByCode(baseCode);
            CurrencyDto targetCurrencyDto = currenciesService.getByCode(targetCode);

            BigDecimal amount = validator.parsAmount(amountDto);
            BigDecimal rate = getRateBasedOnPair(baseCode, targetCode);
            BigDecimal convertedAmount = amount.multiply(rate)
                    .setScale(SCALE, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
            return new ExchangeDto(baseCurrencyDto, targetCurrencyDto, rate, amount, convertedAmount);
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    private BigDecimal getRateBasedOnPair(String baseCode, String targetCode){

        String pair1 = baseCode + targetCode;
        String pair2 = targetCode + baseCode;
        String pair3 = USD_CODE + baseCode;
        String pair4 = USD_CODE + targetCode;

        try {
            return exchangeRatesService.get(pair1)
                    .getRate();
        } catch (Exception _) {}

        try {
            BigDecimal divider = new BigDecimal("1");
            BigDecimal rate = exchangeRatesService.get(pair2).getRate();
            return divider.divide(rate, SCALE, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
        } catch (Exception _) {}

        try {
            BigDecimal UsdToBase = exchangeRatesService.get(pair3).getRate();
            BigDecimal UsdToTarget = exchangeRatesService.get(pair4).getRate();
            return UsdToTarget.divide(UsdToBase, SCALE, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
        } catch (Exception _) {
            throw new BadRequestException(INCORRECT_CONVERSION_INPUT.getMessage());
        }
    }


}
