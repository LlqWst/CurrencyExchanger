package org.example.service;

import org.example.dao.CurrenciesDao;
import org.example.dto.CurrencyDto;
import org.example.entity.Currency;
import org.example.handler.BadRequestException;
import org.example.handler.CurrencyExistException;
import org.example.handler.NotFoundException;

import java.util.List;

public class CurrenciesService {

    CurrenciesDao dao = new CurrenciesDao();

    public Currency get(CurrencyDto dto) {
        try {
            return dao.getByCode(dto.getCode());
        } catch (BadRequestException e) {
            throw new BadRequestException (e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    public List<Currency> getAll() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Currency save(CurrencyDto dto){
        try {
            Currency currency = new Currency();
            currency.setCode(dto.getCode());
            currency.setFullName(dto.getName());
            currency.setSign(dto.getSign());
            return dao.save(currency);
        } catch (CurrencyExistException e){
          throw new CurrencyExistException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
