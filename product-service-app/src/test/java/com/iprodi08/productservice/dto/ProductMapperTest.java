package com.iprodi08.productservice.dto;

import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_1;
import static com.iprodi08.productservice.test_data.DiscountTestData.getDiscountDto;
import static com.iprodi08.productservice.test_data.DurationTestData.DURATION_1;
import static com.iprodi08.productservice.test_data.DurationTestData.getDurationDto;
import static com.iprodi08.productservice.test_data.PriceTestData.PRICE_1;
import static com.iprodi08.productservice.test_data.PriceTestData.getPriceDto;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Test
    void giveProductDtoFromProduct() {
        //given

        final PriceDto actualpriceDto = getPriceDto(PRICE_1);

        final DurationDto actualDurationDto = getDurationDto(DURATION_1);

        final DiscountDto actualDiscountDto = getDiscountDto(DISCOUNT_1);

        final Product product = PRODUCT_1;
        product.setPrice(PRICE_1);
        product.setDuration(DURATION_1);
        product.setDiscounts(List.of(DISCOUNT_1));

        //when

        final ProductDto mapperProductDto = mapper.productToProductDto(product);
        final DurationDto expectedDurationDto = mapperProductDto.getDurationDto();
        final DiscountDto expectedDiscountDto = mapperProductDto.getDiscountDtos().getFirst();

        //then

        assertEquals("Product1", mapperProductDto.getSummary());
        assertEquals("This is product 1", mapperProductDto.getDescription());
        assertEquals(actualpriceDto.getValue(), mapperProductDto.getPriceDto().getValue());
        assertEquals(actualpriceDto.getCurrency(), mapperProductDto.getPriceDto().getCurrency());
        assertEquals(actualDurationDto.getInDays(), expectedDurationDto.getInDays());
        assertTrue(mapperProductDto.getActive());
        assertEquals(actualDiscountDto.getValue(), expectedDiscountDto.getValue());
    }
}
