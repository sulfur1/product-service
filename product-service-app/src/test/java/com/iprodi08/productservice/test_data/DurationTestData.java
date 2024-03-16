package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.DurationDto;
import com.iprodi08.productservice.dto.mapper.DurationMapper;
import com.iprodi08.productservice.entity.Duration;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

public final class DurationTestData {

    public static final Long DURATION_ID_1 = 1L;

    public static final Long DURATION_ID_2 = DURATION_ID_1 + 1;

    public static final Long DURATION_ID_3 = DURATION_ID_1 + 2;

    public static final Integer INDAYS_1 = 30;

    public static final Integer INDAYS_2 = 60;

    public static final Integer INDAYS_3 = 90;

    public static final Long NOT_EXIST_ID = 110L;

    public static final Duration DURATION_1 = new Duration(
            DURATION_ID_1, INDAYS_1,
            null, null, Collections.emptyList()
    );

    public static final Duration DURATION_2 = new Duration(
            DURATION_ID_2, INDAYS_2,
            null, null, Collections.emptyList()
    );

    public static final Duration DURATION_3 = new Duration(
            DURATION_ID_3, INDAYS_3,
            null, null, Collections.emptyList()
    );

    private static final DurationMapper MAPPER = Mappers.getMapper(DurationMapper.class);

    private DurationTestData() {
    }

    public static Duration getNew() {
        return new Duration(INDAYS_3);
    }

    public static List<Duration> getDurations() {
        return List.of(DURATION_1, DURATION_2, DURATION_3);
    }

    public static DurationDto getDurationDto(Duration duration) {
        return MAPPER.durationToDurationDto(duration);
    }
}
