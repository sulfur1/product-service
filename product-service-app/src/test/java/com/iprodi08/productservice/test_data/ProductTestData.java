package com.iprodi08.productservice.test_data;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Product;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

public final class ProductTestData {

    public static final Long PRODUCT_ID_1 = 1L;

    public static final Long PRODUCT_ID_2 = PRODUCT_ID_1 + 1;

    public static final Long PRODUCT_ID_3 = PRODUCT_ID_1 + 2;

    public static final Long NOT_EXIST_ID = 110L;

    public static final Product PRODUCT_1 = new Product(
            PRODUCT_ID_1, "Product1", "This is product 1",
            null, null, true, null, null, Collections.emptyList()
    );

    public static final Product PRODUCT_2 = new Product(
            PRODUCT_ID_2, "Product2", "This is product 2",
            null, null, true, null, null, Collections.emptyList()
    );

    public static final Product PRODUCT_3 = new Product(
            PRODUCT_ID_3, "Product3", "This is product 3",
            null, null, false, null, null, Collections.emptyList()
    );

    private static final ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    private ProductTestData() {
    }

    public static Product getNew() {
        return new Product("Product3", "This is product 3",
                null, null, false);
    }
    public static List<Product> getProducts() {
        return List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3);
    }

    public static ProductDto getProductDto(Product product) {
        return MAPPER.productToProductDto(product);
    }
}
