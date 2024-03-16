package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.dto.mapper.PriceMapper;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.enumType.Currency;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public final class PriceTestData {

    public static final Long PRICE_ID_1 = 1L;

    public static final Long PRICE_ID_2 = PRICE_ID_1 + 1;

    public static final Long PRICE_ID_3 = PRICE_ID_1 + 2;

    public static final BigDecimal PRICE_1_VALUE = BigDecimal.valueOf(234.45);

    public static final BigDecimal PRICE_2_VALUE = BigDecimal.valueOf(500.00);

    public static final BigDecimal PRICE_3_VALUE = BigDecimal.valueOf(70000.00);

    public static final BigDecimal PRICE_NEW_VALUE = BigDecimal.valueOf(50000.00);

    public static final Long NOT_EXIST_ID = 110L;

    public static final Price PRICE_1 = new Price(
            PRICE_ID_1, PRICE_1_VALUE, Currency.USD,
            null, null, Collections.emptyList()
    );

    public static final Price PRICE_2 = new Price(
            PRICE_ID_2, PRICE_2_VALUE, Currency.USD,
            null, null, Collections.emptyList()
    );

    public static final Price PRICE_3 = new Price(
            PRICE_ID_3, PRICE_3_VALUE, Currency.RUB,
            null, null, Collections.emptyList()
    );

    private static final PriceMapper MAPPER = Mappers.getMapper(PriceMapper.class);

    private PriceTestData() {
    }

    public static Price getNew() {
        return new Price(PRICE_NEW_VALUE, Currency.RUB);
    }

    public static List<Price> getPrices() {
        return List.of(PRICE_1, PRICE_2, PRICE_3);
    }

    public static PriceDto getPriceDto(Price price) {
        return MAPPER.priceToPriceDto(price);
    }
}
