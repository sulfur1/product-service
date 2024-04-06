package com.iprodi08.productservice.dto.mapper;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DiscountMapper {
    DiscountDto dicountToDiscountDto(Discount discount);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Discount discountDtoToDiscount(DiscountDto discountDto);
}
