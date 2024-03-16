package com.iprodi08.productservice.dto.mapper;

import com.iprodi08.productservice.dto.DurationDto;
import com.iprodi08.productservice.entity.Duration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DurationMapper {

    DurationDto durationToDurationDto(Duration duration);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Duration durationDtoToDuration(DurationDto durationDto);
}
