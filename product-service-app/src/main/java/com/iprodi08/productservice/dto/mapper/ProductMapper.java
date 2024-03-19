package com.iprodi08.productservice.dto.mapper;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    @Mapping(target = "summary", source = "summary")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "priceDto", source = "price")
    @Mapping(target = "durationDtos", source = "durations")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "discountDtos", source = "discounts")
    ProductDto productToProductDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "summary", source = "summary")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "priceDto")
    @Mapping(target = "durations", source = "durationDtos")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "discounts", source = "discountDtos")
    Product productDtoToProduct(ProductDto productDto);
}