package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.ProductRepository;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.DurationTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import com.iprodi08.productservice.test_data.ProductTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_ID_1;
import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_ID_2;
import static com.iprodi08.productservice.test_data.DiscountTestData.getDiscountDto;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_1;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_2;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_1;
import static com.iprodi08.productservice.test_data.ProductTestData.NOT_EXIST_ID;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_2;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_3;
import static com.iprodi08.productservice.test_data.ProductTestData.getProductDto;
import static com.iprodi08.productservice.test_data.ProductTestData.getProducts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper;

    @InjectMocks
    private SimpleProductService productService;


    @Test
    void getProductByIdExists() {
        Product mockProduct = Mockito.mock(Product.class);
        ProductDto mockProductDto = Mockito.mock(ProductDto.class);

        when(productRepository.findById(PRODUCT_ID_1)).thenReturn(Optional.of(mockProduct));
        when(productMapper.productToProductDto(any(Product.class))).thenReturn(mockProductDto);

        Optional<ProductDto> expected = productService.getProductById(PRODUCT_ID_1);

        assertTrue(expected.isPresent());
        assertSame(mockProductDto, expected.get());
    }

    @Test
    void getProductByIdNotExists() {
        when(productRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        Optional<ProductDto> expected = productService.getProductById(NOT_EXIST_ID);

        assertFalse(expected.isPresent());
    }

    @Test
    void getAllProducts() {
        List<Product> products = getProducts();
        Page<Product> productsPage = new PageImpl<>(products);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productsPage);

        List<ProductDto> expected = productService.getAllProducts(Pageable.ofSize(3));

        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(3);
    }

    @Test
    void getAllProductsByActiveTrue() {
        List<Product> products = List.of(PRODUCT_1, PRODUCT_2);
        Page<Product> productsPage = new PageImpl<>(products);
        Boolean active = true;

        Pageable pageableSize = Pageable.ofSize(3);
        when(productRepository.findAll(any(ProductSpecification.class), any(Pageable.class))).thenReturn(productsPage);

        List<ProductDto> expected = productService.getAllProductsByActive(pageableSize, active);

        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    void getAllProductsByName() {
        List<Product> products = List.of(PRODUCT_1);
        Page<Product> productsPage = new PageImpl<>(products);
        Boolean active = true;

        Pageable pageableSize = Pageable.ofSize(3);
        when(productRepository
                .findAll(any(ProductSpecification.class), any(Pageable.class)))
                .thenReturn(productsPage);

        List<ProductDto> expected = productService.getAllProductsByActive(pageableSize, active);

        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(1);
    }

    @Test
    void createProduct() {
        Price price = PriceTestData.getNew();
        Duration duration = DurationTestData.getNew();
        Product created = ProductTestData.getNew();
        created.setPrice(price);
        created.setDuration(duration);
        ProductDto createdDto = getProductDto(created);

        when(productRepository.save(any(Product.class))).thenReturn(created);
        when(productMapper.productDtoToProduct(any(ProductDto.class))).thenReturn(created);

        Product expected = productService.createProduct(createdDto);

        assertThat(expected).isNotNull();
    }

    @Test
    void updatePriceOfProduct() {
        Price price = PriceTestData.getNew();
        Duration duration = DurationTestData.getNew();
        Product created = ProductTestData.getNew();
        created.setPrice(price);
        created.setDuration(duration);
        ProductDto createdDto = getProductDto(created);

        when(productRepository.findById(PRODUCT_ID_1)).thenReturn(Optional.of(created));

        assertAll(() -> productService.updatePriceOfProduct(PRODUCT_ID_1, createdDto));
    }

    @Test
    void updatePriceOfProductNotFound() {
        ProductDto productDto = ProductTestData.getProductDto(PRODUCT_1);

        when(productRepository.findById(PRODUCT_ID_1)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> productService.updatePriceOfProduct(PRODUCT_ID_1, productDto));
    }

    @Test
    void applyBulkDiscountToAllProducts() {
        Discount applyDiscount = DiscountTestData.getNew();

        Product productOne = ProductTestData.getNew();
        productOne.getDiscounts().add(applyDiscount);

        Product productTwo = ProductTestData.getNew();
        productTwo.getDiscounts().add(applyDiscount);

        when(productRepository.getAllProductsWithDiscounts()).thenReturn(List.of(productOne, productTwo));

        assertAll(() -> productService.applyBulkDiscountToAllProducts(getDiscountDto(applyDiscount)));
    }

    @Test
    void bulkActivateOrDiactivateDiscountForAllProducts() {
        Boolean active = true;

        when(productRepository.getAllProductsWithDiscountsById(DISCOUNT_ID_1)).thenReturn(getProducts());

        assertAll(() -> productService.bulkActivateOrDiactivateDiscountForAllProducts(DISCOUNT_ID_1, active));
    }

    @Test
    void activateOrDiactivateDiscountForProduct() {
        Boolean active = false;

        when(productRepository
                .getProductByIdWithDiscounts(PRODUCT_ID_3, DISCOUNT_ID_2))
                .thenReturn(Optional.of(PRODUCT_1));

        assertAll(() -> productService.activateOrDiactivateDiscountForProduct(DISCOUNT_ID_2, PRODUCT_ID_3, active));
    }

    @Test
    void acivateOrDiactivateProduct() {
        Boolean active = false;

        when(productRepository.findById(PRODUCT_ID_2)).thenReturn(Optional.of(PRODUCT_2));

        assertAll(() -> productService.acivateOrDiactivateProduct(PRODUCT_ID_2, active));
    }
}
