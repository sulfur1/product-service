package com.iprodi08.productservice.dto;

import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static com.iprodi08.productservice.dto.ProductTestData.getDiscountDto;
import static com.iprodi08.productservice.dto.ProductTestData.getDurationDto;
import static com.iprodi08.productservice.dto.ProductTestData.getPriceDto;
import static com.iprodi08.productservice.dto.ProductTestData.getProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Test
    void giveProductDtoFromProduct() {
        final PriceDto priceDto = getPriceDto();

        final DurationDto actualDurationDto = getDurationDto();

        final DiscountDto actualDiscountDto = getDiscountDto();

        final Product product = getProduct();

        final ProductDto mapperProductDto = mapper.productToProductDto(product);

        assertEquals("Product1", mapperProductDto.getSummary());
        assertEquals("This is product 1", mapperProductDto.getDescription());
        assertEquals(priceDto.getValue(), mapperProductDto.getPriceDto().getValue());
        assertEquals(priceDto.getCurrency(), mapperProductDto.getPriceDto().getCurrency());

        final DurationDto expectedDurationDto = mapperProductDto.getDurationDtos().getFirst();
        assertEquals(actualDurationDto.getInDays(), expectedDurationDto.getInDays());

        assertTrue(mapperProductDto.getActive());

        final DiscountDto expectedDiscountDto = mapperProductDto.getDiscountDtos().getFirst();
        assertEquals(actualDiscountDto.getValue(), expectedDiscountDto.getValue());
    }
}
