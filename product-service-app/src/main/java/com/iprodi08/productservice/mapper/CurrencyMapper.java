package com.iprodi08.productservice.mapper;

import com.iprodi08.productservice.dto.CurrencyDto;
import com.iprodi08.productservice.entity.enumType.Currency;
import org.mapstruct.Mapper;

@Mapper
public interface CurrencyMapper {

    default Currency mapCurrency(CurrencyDto currency) {
        return Enum.valueOf(Currency.class, currency.getCurrency());
    }

    default CurrencyDto mapCurrencyDto(Currency currency) {
        return new CurrencyDto(currency.name());
    }
}
