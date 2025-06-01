package dev.lqwd.mapper;

import dev.lqwd.dto.ExchangeRateResponseDto;
import dev.lqwd.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponseDto toResponseDto(ExchangeRate exRate);
}
