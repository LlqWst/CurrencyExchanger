package org.example.mapper;

import org.example.dto.CurrencyDto;
import org.example.dto.ExchangeRateDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDto toCurrencyDto(Currency currency);
    Currency toCurrency(CurrencyDto currencyDto);

    ExchangeRateDto toExchangeRateDto(ExchangeRate exRate);
}
