package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.DurationTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private DurationRepository durationRepository;

    @Autowired
    private DiscountRepository discountRepository;

    private Product actualProduct1;

    private Product actualProduct2;

    private Price actualPrice;

    private Duration actualDuration;

    private Discount actualDiscount1;

    private Discount actualDiscount2;

    private Discount actualDiscount3;

    @BeforeEach
    void setUp() {
        actualPrice = priceRepository.save(PriceTestData.getNew());
        actualDuration = durationRepository.save(DurationTestData.getNew());
        actualDiscount1 = discountRepository.save(DiscountTestData.getNew());
        actualDiscount2 = discountRepository.save(DiscountTestData.getNew());
        actualProduct1 = Product.createNewProduct(
                null,
                "Product1",
                "This is product1",
                true
        );
        actualProduct1.setPrice(actualPrice);
        actualProduct1.setDuration(actualDuration);
        actualProduct1.setDiscounts(List.of(actualDiscount1, actualDiscount2));
        productRepository.save(actualProduct1);

        actualProduct2 = Product.createNewProduct(
                null,
                "Product2",
                "This is product2",
                true);
        actualDiscount3 = discountRepository.save(DiscountTestData.getNew());
        actualProduct2.setPrice(actualPrice);
        actualProduct2.setDuration(actualDuration);
        actualProduct2.setDiscounts(List.of(actualDiscount1, actualDiscount3));
        productRepository.save(actualProduct2);

    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(
                jdbcTemplate,
                "products_discounts",
                "discounts",
                "products",
                "prices",
                "durations");
    }

    @Test
    void getAllProductsWithDiscounts() {
        //given
        //when

        List<Product> expected = productRepository.getAllProductsWithDiscounts();
        List<Discount> expectedDiscount = expected.stream()
                        .flatMap(product -> product.getDiscounts().stream())
                        .toList();

        //then

        assertThat(expected).isNotEmpty();
        assertThat(expected).hasSize(2);
        assertThat(expectedDiscount).isNotEmpty();
        assertThat(expectedDiscount).hasSize(4);
    }

    @Test
    void getAllProductsWithDiscountsById() {
        //given
        //when

        List<Product> expected = productRepository
                .getAllProductsWithDiscountsById(actualDiscount1.getId());
        List<Discount> expectedDiscount = expected.stream()
                .flatMap(product -> product.getDiscounts().stream())
                .filter(discount -> discount.getValue().equals(actualDiscount1.getValue()))
                .toList();

        //then

        assertThat(expected).isNotEmpty();
        assertThat(expected).hasSize(2);
        assertThat(expectedDiscount).isNotEmpty();
        assertThat(expectedDiscount).hasSize(2);
    }

    @Test
    void getProductByIdWithDiscounts() {
        //given

        //when

        Optional<Product> product = productRepository
                .getProductByIdWithDiscounts(actualProduct1.getId());

        //then

        assertTrue(product.isPresent());
        assertEquals(product.get().getId(), actualProduct1.getId());
        assertThat(product.get().getDiscounts()).isNotEmpty();
        assertThat(product.get().getDiscounts()).hasSize(2);
    }

    @Test
    void getProductByIdWithDiscountById() {
        //given

        //when

        Optional<Product> product = productRepository
                .getProductByIdWithDiscountById(actualProduct1.getId(), actualDiscount2.getId());

        //then

        assertTrue(product.isPresent());
        assertEquals(product.get().getId(), actualProduct1.getId());
        assertThat(product.get().getDiscounts()).isNotEmpty();
        assertThat(product.get().getDiscounts()).hasSize(1);
        assertEquals(
                product.get().getDiscounts().getFirst().getId(),
                actualDiscount2.getId()
                );
    }

    @Test
    @Transactional
    void updatePriceForProductById() {
        //given

        Price newPrice = priceRepository.save(PriceTestData.getNew());
        productRepository
                .findById(actualProduct2.getId())
                .ifPresent(product -> product.setPrice(newPrice));

        //when

        Product updatedProduct = productRepository.findById(actualProduct2.getId()).get();

        //then

        assertNotEquals(updatedProduct.getPrice().getId(), actualPrice.getId());
        assertEquals(updatedProduct.getPrice().getId(), newPrice.getId());
    }

    @Test
    @Transactional
    void updateActiveOfProductById() {
        //given

        Boolean active = false;
        productRepository
                .findById(actualProduct2.getId())
                .ifPresent(product -> product.setActive(active));

        //when

        Product updatedProduct = productRepository.findById(actualProduct2.getId()).get();

        //then

        assertFalse(updatedProduct.getActive());
    }
}
