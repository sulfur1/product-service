package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.filter.OperationSpecification;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.repository.filter.SearchCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static com.iprodi08.productservice.test_data.DurationTestData.DURATION_1;
import static com.iprodi08.productservice.test_data.PriceTestData.PRICE_1;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_1;
import static com.iprodi08.productservice.test_data.ProductTestData.PRODUCT_ID_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpecificationTest {

    private static final String RESET_SEQ = """
            ALTER SEQUENCE prices_id_seq RESTART WITH 1;
            ALTER SEQUENCE durations_id_seq RESTART WITH 1;
            ALTER SEQUENCE products_id_seq RESTART WITH 1;
            """;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private DurationRepository durationRepository;

    @BeforeEach
    void setUp() {
        Price expectedPrice = priceRepository.save(PRICE_1);
        Duration expectedDuration = durationRepository.save(DURATION_1);

        Product actualProduct1 = Product.createNewProduct(
                PRODUCT_ID_1,
                "Product1",
                "This is product1",
                true
        );
        actualProduct1.setPrice(expectedPrice);
        actualProduct1.setDuration(expectedDuration);
        productRepository.save(actualProduct1);

        Product actualProduct2 = Product.createNewProduct(
                PRODUCT_ID_2,
                "product2",
                "description",
                false);
        actualProduct2.setPrice(expectedPrice);
        actualProduct2.setDuration(expectedDuration);
        productRepository.save(actualProduct2);

    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(
                jdbcTemplate,
                "products",
                "prices",
                "durations");

        jdbcTemplate.execute(RESET_SEQ);
    }

    @Test
    void findAllProductsByName() {
        String key = "summary";
        String value = "product2";

        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, value);
        ProductSpecification specification = new ProductSpecification(criteria);

        Product expected = productRepository.findAll(specification).getFirst();

        assertThat(expected).isNotNull();
        assertEquals(expected.getId(), PRODUCT_ID_2);
    }

    @Test
    void findAllProductsByActiveFalse() {
        String key = "active";
        Boolean active = false;
        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, String.valueOf(active));
        ProductSpecification specification = new ProductSpecification(criteria);

        Product expected = productRepository.findAll(specification).getFirst();

        assertThat(expected).isNotNull();
        assertEquals(expected.getId(), PRODUCT_ID_2);
    }

    @Test
    void findAllProductsByNameIfNameIsEmpty() {
        String key = "summary";
        String value = "";

        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, value);
        ProductSpecification specification = new ProductSpecification(criteria);

        List<Product> expected = productRepository.findAll(specification);

        assertThat(expected).isNotEmpty();
        assertThat(expected).hasSize(2);
    }

    @Test
    void findAllProductsIfNull() {
        String key = "summary";
        String value = null;

        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, value);
        ProductSpecification specification = new ProductSpecification(criteria);

        List<Product> expected = productRepository.findAll(specification);

        assertThat(expected).isEmpty();
    }

}
