package dev.lqwd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDto {

    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
}

