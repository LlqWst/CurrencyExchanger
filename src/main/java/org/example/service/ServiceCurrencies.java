package org.example.service;

import org.example.dto.CurrencyDto;
import org.example.entity.Currency;

public interface ServiceCurrencies extends Service<CurrencyDto, Integer> {
    CurrencyDto getByCode (String code);
    Currency toCurrency (CurrencyDto dto);
}
