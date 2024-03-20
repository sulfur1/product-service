package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.mapper.DiscountMapper;
import com.iprodi08.productservice.entity.Discount;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public final class DiscountTestData {

    public static final String TIME_ZONE = "+03:00";

    public static final Long DISCOUNT_ID_1 = 1L;

    public static final Long DISCOUNT_ID_2 = DISCOUNT_ID_1 + 1;

    public static final Long DISCOUNT_ID_3 = DISCOUNT_ID_1 + 2;

    public static final Long DISCOUNT_NEW_ID = DISCOUNT_ID_1 + 3;

    public static final Long NOT_EXIST_ID = 110L;

    public static final Long DISCOUNT_1_ACTIVE_OFFSET_1 = 10L;

    public static final Instant DISCOUNT_1_DATE_TIME_FROM = LocalDateTime
            .now()
            .minusDays(DISCOUNT_1_ACTIVE_OFFSET_1)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_2_ACTIVE_OFFSET_2 = 25L;

    public static final Instant DISCOUNT_2_DATE_TIME_FROM = LocalDateTime
            .now()
            .minusDays(DISCOUNT_2_ACTIVE_OFFSET_2)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_3_ACTIVE_OFFSET_3 = 15L;

    public static final Instant DISCOUNT_3_DATE_TIME_FROM = LocalDateTime
            .now()
            .minusDays(DISCOUNT_3_ACTIVE_OFFSET_3)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_1_ACTIVE_TO_1 = 3L;

    public static final Instant DISCOUNT_1_DATE_TIME_UNTIL = LocalDateTime
            .now()
            .minusDays(DISCOUNT_1_ACTIVE_TO_1)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_2_ACTIVE_TO_2 = 5L;

    public static final Instant DISCOUNT_2_DATE_TIME_UNTIL = LocalDateTime
            .now()
            .minusDays(DISCOUNT_2_ACTIVE_TO_2)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Long DISCOUNT_3_ACTIVE_TO_3 = 8L;

    public static final Instant DISCOUNT_3_DATE_TIME_UNTIL = LocalDateTime
            .now()
            .minusDays(DISCOUNT_3_ACTIVE_TO_3)
            .toInstant(ZoneOffset.of(TIME_ZONE));

    public static final Integer DISCOUNT_1_VALUE = 50;

    public static final Integer DISCOUNT_2_VALUE = 30;

    public static final Integer DISCOUNT_3_VALUE = 48;

    public static final Integer DISCOUNT_NEW_VALUE = 60;

    public static final Discount DISCOUNT_1 = Discount.createNewDiscount(
            DISCOUNT_ID_1,
            DISCOUNT_1_VALUE,
            DISCOUNT_1_DATE_TIME_FROM,
            DISCOUNT_1_DATE_TIME_UNTIL,
            true);

    public static final Discount DISCOUNT_2 = Discount.createNewDiscount(
            DISCOUNT_ID_2,
            DISCOUNT_2_VALUE,
            DISCOUNT_2_DATE_TIME_FROM,
            DISCOUNT_2_DATE_TIME_UNTIL,
            true);

    public static final Discount DISCOUNT_3 = Discount.createNewDiscount(
            DISCOUNT_ID_3,
            DISCOUNT_3_VALUE,
            DISCOUNT_3_DATE_TIME_FROM,
            DISCOUNT_3_DATE_TIME_UNTIL,
            true);

    private static final DiscountMapper MAPPER = Mappers.getMapper(DiscountMapper.class);

    private DiscountTestData() {
    }

    public static Discount getNew() {
        return Discount.createNewDiscount(
                DISCOUNT_NEW_ID,
                DISCOUNT_NEW_VALUE,
                DISCOUNT_3_DATE_TIME_FROM,
                DISCOUNT_3_DATE_TIME_UNTIL,
                true
        );
    }

    public static List<Discount> getDiscounts() {
        return List.of(DISCOUNT_1, DISCOUNT_2, DISCOUNT_3);
    }

    public static DiscountDto getDiscountDto(Discount discount) {
        return MAPPER.dicountToDiscountDto(discount);
    }
}
