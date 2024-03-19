package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.mapper.DiscountMapper;
import com.iprodi08.productservice.entity.Discount;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

public final class DiscountTestData {

    public static final Long DISCOUNT_ID_1 = 1L;

    public static final Long DISCOUNT_ID_2 = DISCOUNT_ID_1 + 1;

    public static final Long DISCOUNT_ID_3 = DISCOUNT_ID_1 + 2;

    public static final Long NOT_EXIST_ID = 110L;

    public static final Long DISCOUNT_ACTIVE_OFFSET_1 = 10L;

    public static final Long DISCOUNT_ACTIVE_OFFSET_2 = 25L;

    public static final Long DISCOUNT_ACTIVE_OFFSET_3 = 15L;

    public static final Long DISCOUNT_ACTIVE_TO_1 = 3L;

    public static final Long DISCOUNT_ACTIVE_TO_2 = 5L;

    public static final Long DISCOUNT_ACTIVE_TO_3 = 8L;

    public static final Integer DISCOUNT_1_VALUE = 50;

    public static final Integer DISCOUNT_2_VALUE = 30;

    public static final Integer DISCOUNT_3_VALUE = 48;

    public static final Integer DISCOUNT_NEW_VALUE = 60;

    public static final Discount DISCOUNT_1 = new Discount(
            DISCOUNT_ID_1, DISCOUNT_1_VALUE, OffsetDateTime.now().minusDays(DISCOUNT_ACTIVE_OFFSET_1),
            OffsetDateTime.now().plusDays(DISCOUNT_ACTIVE_TO_1),
            true,  null, null, Collections.emptyList()
    );

    public static final Discount DISCOUNT_2 = new Discount(
            DISCOUNT_ID_2, DISCOUNT_2_VALUE, OffsetDateTime.now().minusDays(DISCOUNT_ACTIVE_OFFSET_2),
            OffsetDateTime.now().plusDays(DISCOUNT_ACTIVE_TO_2),
            true,  null, null, Collections.emptyList()
    );

    public static final Discount DISCOUNT_3 = new Discount(
            DISCOUNT_ID_3, DISCOUNT_3_VALUE, OffsetDateTime.now().minusDays(DISCOUNT_ACTIVE_OFFSET_3),
            OffsetDateTime.now().plusDays(DISCOUNT_ACTIVE_TO_3),
            true,  null, null, Collections.emptyList()
    );

    private static final DiscountMapper MAPPER = Mappers.getMapper(DiscountMapper.class);

    private DiscountTestData() {
    }

    public static Discount getNew() {
        return new Discount(DISCOUNT_NEW_VALUE, OffsetDateTime.now().minusDays(DISCOUNT_ACTIVE_OFFSET_3),
                OffsetDateTime.now().plusDays(DISCOUNT_ACTIVE_TO_3), true);
    }

    public static List<Discount> getDiscounts() {
        return List.of(DISCOUNT_1, DISCOUNT_2, DISCOUNT_3);
    }

    public static DiscountDto getDiscountDto(Discount discount) {
        return MAPPER.dicountToDiscountDto(discount);
    }
}
