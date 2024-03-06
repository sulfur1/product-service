package com.iprodi08.productservice.dto.mapper;

import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.entity.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PriceMapper {

    PriceDto priceToPriceDto(Price price);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Price priceDtoToPrice(PriceDto priceDto);
}
