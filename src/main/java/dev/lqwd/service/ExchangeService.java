package dev.lqwd.service;

import dev.lqwd.dto.CurrencyDto;
import dev.lqwd.dto.ExchangeDto;
import dev.lqwd.dto.ExchangePairDto;
import dev.lqwd.exceptions.CurrenciesExceptions;
import dev.lqwd.exceptions.custom_exceptions.BadRequestException;
import dev.lqwd.exceptions.custom_exceptions.DataBaseException;
import dev.lqwd.validator.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static dev.lqwd.exceptions.ErrorMessages.INCORRECT_CONVERSION_INPUT;
import static dev.lqwd.exceptions.ErrorMessages.INTERNAL_ERROR;

public class ExchangeService {

    private final static String USD_CODE = "USD";
    private final static int SCALE = Validator.SCALE;
    private final static int VIEW_SCALE = 2;

    private final ExchangeRatesService exchangeRatesService;
    private final CurrenciesService currenciesService;

    public ExchangeService() {
        this.exchangeRatesService = new ExchangeRatesService();
        this.currenciesService = new CurrenciesService();
    }

    public ExchangeDto get(ExchangePairDto exPairDto) {
        try {
            String baseCode = exPairDto.getBaseCurrencyCode();
            String targetCode = exPairDto.getTargetCurrencyCode();
            BigDecimal amount = exPairDto.getAmount();

            CurrencyDto baseCurrencyDto = currenciesService.getByCode(baseCode);
            CurrencyDto targetCurrencyDto = currenciesService.getByCode(targetCode);

            BigDecimal rate = getRateBaseOnPair(baseCode, targetCode);
            BigDecimal convertedAmount = amount.multiply(rate)
                    .setScale(VIEW_SCALE, RoundingMode.HALF_UP);
            return new ExchangeDto(baseCurrencyDto,
                    targetCurrencyDto,
                    rate.setScale(VIEW_SCALE, RoundingMode.HALF_UP),
                    amount.setScale(VIEW_SCALE, RoundingMode.HALF_UP),
                    convertedAmount
            );
        } catch (CurrenciesExceptions e) {
            throw new CurrenciesExceptions (e.getStatusCode(), e.getMessage());
        } catch (Exception e){
            throw new DataBaseException(INTERNAL_ERROR.getMessage());
        }
    }

    private BigDecimal getRateBaseOnPair(String baseCode, String targetCode){

        String baseToTarget = baseCode + targetCode;
        String targetToBase = targetCode + baseCode;
        String usdToBase = USD_CODE + baseCode;
        String usdToTarget = USD_CODE + targetCode;

        try {
            return exchangeRatesService.get(baseToTarget)
                    .getRate();
        } catch (Exception ignored) {}

        try {
            BigDecimal divider = new BigDecimal("1");
            BigDecimal rate = exchangeRatesService.get(targetToBase).getRate();
            return divider.divide(rate, SCALE, RoundingMode.HALF_UP);
        } catch (Exception ignored) {}

        try {
            BigDecimal UsdToBaseRate = exchangeRatesService.get(usdToBase).getRate();
            BigDecimal UsdToTargetRate = exchangeRatesService.get(usdToTarget).getRate();
            return UsdToTargetRate.divide(UsdToBaseRate, SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new BadRequestException(INCORRECT_CONVERSION_INPUT.getMessage());
        }
    }

}
