package dev.lqwd.mapper;

import dev.lqwd.dto.CurrencyDto;
import dev.lqwd.dto.ExchangeRateDto;
import dev.lqwd.entity.Currency;
import dev.lqwd.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyAndRateMapper {
    CurrencyAndRateMapper INSTANCE = Mappers.getMapper(CurrencyAndRateMapper.class);

    CurrencyDto toCurrencyDto(Currency currency);
    Currency toCurrency(CurrencyDto currencyDto);

    ExchangeRateDto toExchangeRateDto(ExchangeRate exRate);
}
