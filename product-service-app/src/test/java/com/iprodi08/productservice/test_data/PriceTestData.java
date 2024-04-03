package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.dto.mapper.PriceMapper;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.enumType.Currency;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

public final class PriceTestData {

    public static final Long PRICE_ID_1 = 1L;

    public static final Long PRICE_ID_2 = PRICE_ID_1 + 1;

    public static final Long PRICE_ID_3 = PRICE_ID_1 + 2;

    public static final Long PRICE_NEW_ID = PRICE_ID_1 + 3;

    public static final BigDecimal PRICE_1_VALUE = BigDecimal.valueOf(234.45);

    public static final BigDecimal PRICE_2_VALUE = BigDecimal.valueOf(500.00);

    public static final BigDecimal PRICE_NEW_VALUE = BigDecimal.valueOf(312.00);

    public static final Long NOT_EXIST_ID = 110L;

    private static final PriceMapper MAPPER = Mappers.getMapper(PriceMapper.class);

    private PriceTestData() {
    }

    public static Price getNewPrice1() {
        return Price.createNewPrice(
                null,
                PRICE_1_VALUE,
                Currency.USD
        );
    }

    public static Price getNewPrice2() {
        return Price.createNewPrice(
                null,
                PRICE_2_VALUE,
                Currency.USD
        );
    }

    public static Price getNewPrice() {
        return Price.createNewPrice(
                null,
                PRICE_NEW_VALUE,
                Currency.USD
        );
    }

    public static PriceDto getPriceDto(Price price) {
        return MAPPER.priceToPriceDto(price);
    }
}
