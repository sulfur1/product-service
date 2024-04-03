package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.PriceRepository;
import com.iprodi08.productservice.repository.ProductRepository;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.DurationTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import com.iprodi08.productservice.test_data.ProductTestData;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;
import java.util.Optional;

import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_ID_1;
import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_ID_2;
import static com.iprodi08.productservice.test_data.DiscountTestData.getDiscountDto;
import static com.iprodi08.productservice.test_data.PriceTestData.PRICE_1;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_1;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_2;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_1;
import static com.iprodi08.productservice.test_data.ProductTestData.NOT_EXIST_ID;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_2;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_3;
import static com.iprodi08.productservice.test_data.ProductTestData.getProductDto;
import static com.iprodi08.productservice.test_data.ProductTestData.getProducts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
class SimpleProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceRepository priceRepository;

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @InjectMocks
    private SimpleProductService productService;

    @Test
    void getProductByIdExists() {
        //given

        when(productRepository.findById(PRODUCT_ID_1)).thenReturn(Optional.of(PRODUCT_1));

        //when

        Optional<ProductDto> expected = productService.getProductById(PRODUCT_ID_1);

        //then

        assertThat(expected).isNotEmpty();
    }

    @Test
    void getProductByIdNotExists() {
        //given

        when(productRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        //when

        Optional<ProductDto> expected = productService.getProductById(NOT_EXIST_ID);

        //then

        assertFalse(expected.isPresent());
    }

    @Test
    void getAllProducts() {
        //given

        List<Product> products = getProducts();
        Page<Product> productsPage = new PageImpl<>(products);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productsPage);

        //when

        List<ProductDto> expected = productService.getAllProducts(Pageable.ofSize(3));

        //then

        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(3);
        List<ProductDto> actual = products.stream().map(ProductTestData::getProductDto).toList();
        assertEquals(actual, expected);
    }

    @Test
    void getAllProductsByActiveTrue() {
        //given

        List<Product> products = List.of(PRODUCT_1, PRODUCT_2);
        Page<Product> productsPage = new PageImpl<>(products);
        boolean active = true;
        Pageable pageableSize = Pageable.ofSize(3);
        when(productRepository.findAll(any(ProductSpecification.class), any(Pageable.class))).thenReturn(productsPage);

        //when

        List<ProductDto> expected = productService.getAllProductsByActive(pageableSize, active);

        //then

        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    void getAllProductsByName() {
        //given

        List<Product> products = List.of(PRODUCT_1);
        Page<Product> productsPage = new PageImpl<>(products);
        boolean active = true;
        Pageable pageableSize = Pageable.ofSize(3);
        when(productRepository
                .findAll(any(ProductSpecification.class), any(Pageable.class)))
                .thenReturn(productsPage);

        //when

        List<ProductDto> expected = productService.getAllProductsByActive(pageableSize, active);

        //then

        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(1);
    }

    @Test
    void createProduct() {
        //given

        Price price = PriceTestData.getNew();
        Duration duration = DurationTestData.getNew();
        Product created = ProductTestData.getNew();
        created.setPrice(price);
        created.setDuration(duration);
        ProductDto createdDto = getProductDto(created);
        when(productRepository.save(any(Product.class))).thenReturn(created);

        //when

        Product expected = productService.createProduct(createdDto);

        //then

        assertThat(expected).isNotNull();
    }

    @Test
    void updatePriceOfProduct() {
        //given

        Price updatePrice = PriceTestData.getNew();
        Product updatedProduct = ProductTestData.getNew();
        updatedProduct.setId(PRODUCT_ID_1);
        updatedProduct.setPrice(updatePrice);
        ProductDto updatedDto = getProductDto(updatedProduct);
        when(productRepository.findById(PRODUCT_ID_1)).thenReturn(Optional.of(updatedProduct));
        when(priceRepository.save(any(Price.class))).thenReturn(PRICE_1);
        doNothing().when(productRepository).updatePriceForProductById(PRODUCT_ID_1, PRICE_1);

        //when

        Optional<ProductDto> expected = productService.updatePriceOfProduct(updatedDto);

        //then

        assertTrue(expected.isPresent());
        assertEquals(expected.get().getPriceDto().getCurrency(), updatePrice.getCurrency());
    }

    @Test
    void updatePriceOfProductNotFound() {
        //given

        Price price = PriceTestData.getNew();
        Duration duration = DurationTestData.getNew();
        Product created = ProductTestData.getNew();
        created.setId(NOT_EXIST_ID);
        created.setPrice(price);
        created.setDuration(duration);
        ProductDto createdDto = getProductDto(created);
        when(productRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        //when

        Optional<ProductDto> expected = productService.updatePriceOfProduct(createdDto);

        //then

        assertFalse(expected.isPresent());
    }

    @Test
    void applyDiscountToProduct() {
        //given

        Discount applyDiscount = DiscountTestData.getNew();
        Discount discount = DiscountTestData.getNew();
        Product product = ProductTestData.getNew();
        product.getDiscounts().add(discount);

        when(productRepository.getProductByIdWithDiscounts(PRODUCT_ID_1)).thenReturn(Optional.of(product));

        //when

        productService.applyDiscountToProduct(PRODUCT_ID_1, getDiscountDto(applyDiscount));

        //then

        verify(productRepository, times(1))
                .getProductByIdWithDiscounts(PRODUCT_ID_1);
    }

    @Test
    void applyBulkDiscountToAllProducts() {
        //given

        Discount applyDiscount = DiscountTestData.getNew();
        Product productOne = ProductTestData.getNew();
        productOne.getDiscounts().add(applyDiscount);
        Product productTwo = ProductTestData.getNew();
        productTwo.getDiscounts().add(applyDiscount);

        when(productRepository.getAllProductsWithDiscounts()).thenReturn(List.of(productOne, productTwo));

        //when

        productService.applyBulkDiscountToAllProducts(getDiscountDto(applyDiscount));

        //then

        verify(productRepository, times(1))
                .getAllProductsWithDiscounts();
    }

    @Test
    void activateDiscountForAllProducts() {
        //given

        when(productRepository.getAllProductsWithDiscountsById(DISCOUNT_ID_1)).thenReturn(getProducts());

        //when

        productService.activateDiscountForAllProducts(DISCOUNT_ID_1);

        //then

        verify(productRepository, times(1))
                .getAllProductsWithDiscountsById(DISCOUNT_ID_1);
    }

    @Test
    void diactivateDiscountForAllProducts() {
        //given

        when(productRepository.getAllProductsWithDiscountsById(DISCOUNT_ID_1)).thenReturn(getProducts());

        //when

        productService.diactivateDiscountForAllProducts(DISCOUNT_ID_1);

        //then

        verify(productRepository, times(1))
                .getAllProductsWithDiscountsById(DISCOUNT_ID_1);
    }


    @Test
    void activateDiscountForProduct() {
        //given

        Discount discount = DiscountTestData.getNew();
        Product product = ProductTestData.getNew();
        product.getDiscounts().add(discount);
        when(productRepository
                .getProductByIdWithDiscountById(PRODUCT_ID_3, DISCOUNT_ID_2))
                .thenReturn(Optional.of(product));

        //when

        productService.activateDiscountForProduct(DISCOUNT_ID_2, PRODUCT_ID_3);

        //then

        verify(productRepository, times(1))
                .getProductByIdWithDiscountById(PRODUCT_ID_3, DISCOUNT_ID_2);
    }

    @Test
    void diactivateDiscountForProduct() {
        //given

        Discount discount = DiscountTestData.getNew();
        Product product = ProductTestData.getNew();
        product.getDiscounts().add(discount);
        when(productRepository
                .getProductByIdWithDiscountById(PRODUCT_ID_3, DISCOUNT_ID_2))
                .thenReturn(Optional.of(product));

        //when

        productService.diactivateDiscountForProduct(DISCOUNT_ID_2, PRODUCT_ID_3);

        //then

        verify(productRepository, times(1))
                .getProductByIdWithDiscountById(PRODUCT_ID_3, DISCOUNT_ID_2);
    }

    @Test
    void activateProduct() {
        //given

        when(productRepository.findById(PRODUCT_ID_2)).thenReturn(Optional.of(PRODUCT_2));
        doNothing().when(productRepository).updateActiveOfProductById(PRODUCT_ID_2, true);

        //when

        productService.acivateProduct(PRODUCT_ID_2);

        //then

        verify(productRepository, times(1))
                .updateActiveOfProductById(PRODUCT_ID_2, true);
    }

    @Test
    void activateProductIfProductNotFound() {
        //given

        when(productRepository.findById(PRODUCT_ID_2)).thenReturn(Optional.empty());
        doNothing().when(productRepository).updateActiveOfProductById(NOT_EXIST_ID, true);

        //when

        productService.acivateProduct(PRODUCT_ID_2);

        //then

        verify(productRepository, times(0))
                .updateActiveOfProductById(PRODUCT_ID_2, true);
    }

    @Test
    void diactivateProduct() {
        //given

        when(productRepository.findById(PRODUCT_ID_2)).thenReturn(Optional.of(PRODUCT_2));
        doNothing().when(productRepository).updateActiveOfProductById(PRODUCT_ID_2, false);

        //when

        productService.diactivateProduct(PRODUCT_ID_2);

        //then

        verify(productRepository, times(1))
                .updateActiveOfProductById(PRODUCT_ID_2, false);
    }
}
