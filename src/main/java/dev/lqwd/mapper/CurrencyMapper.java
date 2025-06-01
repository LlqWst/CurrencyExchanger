package dev.lqwd.mapper;

import dev.lqwd.dto.CurrencyResponseDto;
import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyResponseDto toResponseDto(Currency currency);

    Currency toCurrency(CurrencyRequestDto currencyRequestDto);
}
