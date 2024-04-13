package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.DurationDto;
import com.iprodi08.productservice.mapper.DurationMapper;
import com.iprodi08.productservice.entity.Duration;
import org.mapstruct.factory.Mappers;
import java.util.List;

public final class DurationTestData {

    public static final Long DURATION_ID_1 = 1L;

    public static final Long DURATION_ID_2 = DURATION_ID_1 + 1;

    public static final Long DURATION_ID_3 = DURATION_ID_1 + 2;

    public static final Long DURATION_NEW_ID = DURATION_ID_1 + 3;

    public static final Integer IN_DAYS_1 = 30;

    public static final Integer IN_DAYS_2 = 60;

    public static final Integer IN_DAYS_3 = 90;

    public static final Long NOT_EXIST_ID = 110L;

    public static final Duration DURATION_1 = Duration.createNewDuration(
            DURATION_ID_1,
            IN_DAYS_1
    );

    public static final Duration DURATION_2 = Duration.createNewDuration(
            DURATION_ID_2,
            IN_DAYS_2
    );

    public static final Duration DURATION_3 = Duration.createNewDuration(
            DURATION_ID_3,
            IN_DAYS_3
    );

    private static final DurationMapper MAPPER = Mappers.getMapper(DurationMapper.class);

    private DurationTestData() {
    }

    public static Duration getNewDuration() {
        return Duration.createNewDuration(null, IN_DAYS_3);
    }

    public static List<Duration> getDurations() {
        return List.of(DURATION_1, DURATION_2, DURATION_3);
    }

    public static DurationDto getDurationDto(Duration duration) {
        return MAPPER.durationToDurationDto(duration);
    }
}
