package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.mapper.DiscountMapper;
import com.iprodi08.productservice.entity.Discount;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class DiscountTestData {

    public static final String TIME_ZONE = "+00:00";

    public static final Long DISCOUNT_ID_1 = 1L;

    public static final Long DISCOUNT_ID_2 = DISCOUNT_ID_1 + 1;

    public static final Long DISCOUNT_ID_3 = DISCOUNT_ID_1 + 2;

    public static final Long NOT_EXIST_ID = 110L;

    public static final Long DISCOUNT_1_ACTIVE_OFFSET_1 = 1L;

    public static final Long DISCOUNT_2_ACTIVE_OFFSET_2 = 2L;

    public static final Long DISCOUNT_3_ACTIVE_OFFSET_3 = 3L;

    public static final Long DISCOUNT_1_ACTIVE_TO_1 = 3L;

    public static final Instant DISCOUNT_1_DATE_TIME_UNTIL = LocalDateTime
            .now()
            .plusDays(DISCOUNT_1_ACTIVE_TO_1)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_2_ACTIVE_TO_2 = 5L;

    public static final Instant DISCOUNT_2_DATE_TIME_UNTIL = LocalDateTime
            .now()
            .plusDays(DISCOUNT_2_ACTIVE_TO_2)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_3_ACTIVE_TO_3 = 8L;

    public static final Instant DISCOUNT_3_DATE_TIME_UNTIL = LocalDateTime
            .now()
            .plusDays(DISCOUNT_3_ACTIVE_TO_3)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Integer DISCOUNT_1_VALUE = 5;

    public static final Integer DISCOUNT_2_VALUE = 10;

    public static final Integer DISCOUNT_3_VALUE = 20;

    public static final Integer DISCOUNT_NEW_VALUE = 3;

    private static final DiscountMapper MAPPER = Mappers.getMapper(DiscountMapper.class);

    private DiscountTestData() {
    }

    public static Discount getNewDiscount1() {
        return Discount.createNewDiscount(
                null,
                DISCOUNT_1_VALUE,
                LocalDateTime
                        .now()
                        .plusHours(DISCOUNT_1_ACTIVE_OFFSET_1)
                        .toInstant(ZoneOffset.of(TIME_ZONE)),
                DISCOUNT_1_DATE_TIME_UNTIL,
                true
        );
    }

    public static Discount getNewDiscount2() {
        return Discount.createNewDiscount(
                null,
                DISCOUNT_2_VALUE,
                LocalDateTime
                        .now()
                        .plusHours(DISCOUNT_2_ACTIVE_OFFSET_2)
                        .toInstant(ZoneOffset.of(TIME_ZONE)),
                DISCOUNT_2_DATE_TIME_UNTIL,
                true
        );
    }

    public static Discount getNewDiscount3() {
        return Discount.createNewDiscount(
                null,
                DISCOUNT_3_VALUE,
                LocalDateTime
                        .now()
                        .plusHours(DISCOUNT_3_ACTIVE_OFFSET_3)
                        .toInstant(ZoneOffset.of(TIME_ZONE)),
                DISCOUNT_3_DATE_TIME_UNTIL,
                true
        );
    }

    public static Discount getNewDiscount4() {
        return Discount.createNewDiscount(
                null,
                DISCOUNT_NEW_VALUE,
                LocalDateTime
                        .now()
                        .plusHours(DISCOUNT_3_ACTIVE_OFFSET_3)
                        .toInstant(ZoneOffset.of(TIME_ZONE)),
                DISCOUNT_3_DATE_TIME_UNTIL,
                true
        );
    }

    public static DiscountDto getDiscountDto(Discount discount) {
        return MAPPER.dicountToDiscountDto(discount);
    }
}
