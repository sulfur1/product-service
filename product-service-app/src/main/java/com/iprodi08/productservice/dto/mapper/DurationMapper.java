package com.iprodi08.productservice.dto.mapper;

import com.iprodi08.productservice.dto.DurationDto;
import com.iprodi08.productservice.entity.Duration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DurationMapper {

    DurationDto durationToDurationDto(Duration duration);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Duration durationDtoToDuration(DurationDto durationDto);
}
