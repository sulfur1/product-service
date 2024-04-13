package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.mapper.DiscountMapper;
import com.iprodi08.productservice.mapper.PriceMapper;
import com.iprodi08.productservice.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.DiscountRepository;
import com.iprodi08.productservice.repository.DurationRepository;
import com.iprodi08.productservice.repository.PriceRepository;
import com.iprodi08.productservice.repository.ProductRepository;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.ProductTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static com.iprodi08.productservice.test_data.DiscountTestData.getNewDiscount1;
import static com.iprodi08.productservice.test_data.DurationTestData.DURATION_ID_1;
import static com.iprodi08.productservice.test_data.DurationTestData.getNewDuration;
import static com.iprodi08.productservice.test_data.PriceTestData.PRICE_ID_1;
import static com.iprodi08.productservice.test_data.PriceTestData.getNewPrice1;
import static com.iprodi08.productservice.test_data.PriceTestData.getPriceDto;
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

@ExtendWith(MockitoExtension.class)
class SimpleProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private DurationRepository durationRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Spy
    private PriceMapper priceMapper = Mappers.getMapper(PriceMapper.class);

    @Spy
    private DiscountMapper discountMapper = Mappers.getMapper(DiscountMapper.class);

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

        Price price = getNewPrice1();
        price.setId(PRICE_ID_1);
        Duration duration = getNewDuration();
        duration.setId(DURATION_ID_1);
        Discount discount = getNewDiscount1();
        discount.setId(DISCOUNT_ID_1);
        Product created = ProductTestData.getNewProduct();
        created.setId(PRODUCT_ID_1);
        created.setPrice(price);
        created.setDuration(duration);
        created.setDiscounts(List.of(discount));
        ProductDto createdDto = getProductDto(created);
        when(productRepository.save(any(Product.class))).thenReturn(created);

        //when

        ProductDto expected = productService.createProduct(createdDto);

        //then

        assertThat(expected).isNotNull();
        assertEquals(expected, createdDto);
    }

    @Test
    void updatePriceOfProduct() {
        //given

        Price price = getNewPrice1();
        price.setId(PRICE_ID_1);
        Product updatedProduct = ProductTestData.getNewProduct();
        updatedProduct.setId(PRODUCT_ID_1);
        updatedProduct.setPrice(price);
        ProductDto updatedDto = getProductDto(updatedProduct);
        when(productRepository.findById(PRODUCT_ID_1)).thenReturn(Optional.of(updatedProduct));
        when(priceRepository.save(any(Price.class))).thenReturn(price);
        doNothing().when(productRepository).updatePriceForProductById(PRODUCT_ID_1, price);

        //when

        Optional<ProductDto> expected = productService.updatePriceOfProduct(updatedDto);

        //then
        PriceDto updatedPriceDto = getPriceDto(price);
        assertTrue(expected.isPresent());
        assertEquals(expected.get().getPriceDto().getCurrency(), updatedPriceDto.getCurrency());
    }

    @Test
    void updatePriceOfProductNotFound() {
        //given

        Price price = getNewPrice1();
        Duration duration = getNewDuration();
        Product created = ProductTestData.getNewProduct();
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

        Discount discount = DiscountTestData.getNewDiscount1();
        Product product = ProductTestData.getNewProduct();
        product.getDiscounts().add(discount);
        DiscountDto actual = getDiscountDto(discount);

        when(productRepository.getProductByIdWithDiscounts(PRODUCT_ID_1)).thenReturn(Optional.of(product));
        when(discountRepository.save(any(Discount.class))).thenReturn(discount);

        //when

        Optional<DiscountDto> expected = productService.applyDiscountToProduct(
                PRODUCT_ID_1,
                actual
        );

        //then

        assertThat(expected).isNotEmpty();
        assertEquals(actual.getValue(), expected.get().getValue());
        assertEquals(actual.getActive(), expected.get().getActive());
        assertEquals(actual.getDateTimeFrom(), expected.get().getDateTimeFrom());
        assertEquals(actual.getDateTimeUntil(), expected.get().getDateTimeUntil());

    }

    @Test
    void applyBulkDiscountToAllProducts() {
        //given

        Discount applyDiscount = DiscountTestData.getNewDiscount1();
        Product productOne = ProductTestData.getNewProduct();
        productOne.getDiscounts().add(applyDiscount);
        Product productTwo = ProductTestData.getNewProduct();
        productTwo.getDiscounts().add(applyDiscount);
        DiscountDto actual = getDiscountDto(applyDiscount);
        when(productRepository.getAllProductsWithDiscounts()).thenReturn(List.of(productOne, productTwo));
        when(discountRepository.save(any(Discount.class))).thenReturn(applyDiscount);

        //when

        Optional<DiscountDto> expected = productService.applyBulkDiscountToAllProducts(actual);

        //then

        assertThat(expected).isNotEmpty();
        assertEquals(actual.getValue(), expected.get().getValue());
        assertEquals(actual.getActive(), expected.get().getActive());
        assertEquals(actual.getDateTimeFrom(), expected.get().getDateTimeFrom());
        assertEquals(actual.getDateTimeUntil(), expected.get().getDateTimeUntil());
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

        Discount discount = DiscountTestData.getNewDiscount1();
        Product product = ProductTestData.getNewProduct();
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

        Discount discount = DiscountTestData.getNewDiscount1();
        Product product = ProductTestData.getNewProduct();
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

        when(productRepository.findById(NOT_EXIST_ID)).thenReturn(Optional.empty());

        //when

        productService.acivateProduct(NOT_EXIST_ID);

        //then

        verify(productRepository, times(1))
                .findById(NOT_EXIST_ID);
        verify(productRepository, times(0))
                .updateActiveOfProductById(NOT_EXIST_ID, true);
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
