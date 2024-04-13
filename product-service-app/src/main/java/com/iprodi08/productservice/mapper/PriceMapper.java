package com.iprodi08.productservice.mapper;

import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.entity.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceMapper extends CurrencyMapper {

    @Mapping(target = "currency", source = "currency")
    PriceDto priceToPriceDto(Price price);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "currency", source = "currency")
    Price priceDtoToPrice(PriceDto priceDto);
}
