package dev.lqwd.service;

import dev.lqwd.dao.ExchangeRateDao;
import dev.lqwd.dto.ExchangeRequestDto;
import dev.lqwd.dto.ExchangeResponseDto;
import dev.lqwd.entity.ExchangeRate;
import dev.lqwd.exceptions.BadRequestException;
import dev.lqwd.mapper.CurrencyMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {

    private final static String USD = "USD";
    private final static int SCALE = 6;
    private final static int VIEW_SCALE = 2;
    private final static String NO_COURSE_EXIST = "This currency pair does not exist. There is no cross-course set";

    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeService() {

        this.exchangeRateDao = new ExchangeRateDao();
    }

    public ExchangeResponseDto get(ExchangeRequestDto requestDto) {

        String baseCode = requestDto.getBaseCurrencyCode();
        String targetCode = requestDto.getTargetCurrencyCode();
        BigDecimal amount = requestDto.getAmount();

        ExchangeRate exchangeRate = getRateBaseOnPair(baseCode, targetCode)
                .orElseThrow(() -> new BadRequestException(NO_COURSE_EXIST));

        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate())
                .setScale(VIEW_SCALE, RoundingMode.HALF_EVEN);

        return new ExchangeResponseDto(
                currencyMapper.toResponseDto(exchangeRate.getBaseCurrency()),
                currencyMapper.toResponseDto(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate().setScale(VIEW_SCALE, RoundingMode.HALF_EVEN),
                amount.setScale(VIEW_SCALE, RoundingMode.HALF_EVEN),
                convertedAmount
        );
    }

    private Optional<ExchangeRate> getRateBaseOnPair(String baseCode, String targetCode) {

        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByPair(baseCode, targetCode);

        if (exchangeRate.isEmpty()) {
            exchangeRate = getIndirectRate(baseCode, targetCode);
        }

        if (exchangeRate.isEmpty()) {
            exchangeRate = getCrossRate(baseCode, targetCode);
        }

        return exchangeRate;
    }

    private Optional<ExchangeRate> getIndirectRate(String baseCode, String targetCode) {

        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByPair(targetCode, baseCode);

        if (exchangeRate.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal indirectRate = exchangeRate.get().getRate();
        BigDecimal rate = BigDecimal.ONE.divide(indirectRate, SCALE, RoundingMode.HALF_EVEN);

        return Optional.of(new ExchangeRate(
                exchangeRate.get().getTargetCurrency(),
                exchangeRate.get().getBaseCurrency(),
                rate
        ));
    }

    private Optional<ExchangeRate> getCrossRate(String baseCode, String targetCode) {

        Optional<ExchangeRate> UsdToBaseRate = exchangeRateDao.getByPair(USD, targetCode);
        Optional<ExchangeRate> UsdToTargetRate = exchangeRateDao.getByPair(USD, baseCode);

        if (UsdToBaseRate.isEmpty() || UsdToTargetRate.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal rate = UsdToTargetRate.get().getRate()
                .divide(UsdToBaseRate.get().getRate(), SCALE, RoundingMode.HALF_EVEN);

        return Optional.of(new ExchangeRate(
                UsdToBaseRate.get().getTargetCurrency(),
                UsdToTargetRate.get().getTargetCurrency(),
                rate
        ));
    }

}
