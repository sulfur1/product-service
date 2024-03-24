package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.filter.OperationSpecification;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.repository.filter.SearchCriteria;
import com.iprodi08.productservice.test_data.DurationTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpecificationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private DurationRepository durationRepository;

    private Product actualProduct1;

    private Product actualProduct2;

    @BeforeEach
    void setUp() {
        Price actualPrice = priceRepository.save(PriceTestData.getNew());
        Duration actualDuration = durationRepository.save(DurationTestData.getNew());

        actualProduct1 = Product.createNewProduct(
                null,
                "Product1",
                "This is product1",
                false
        );
        actualProduct1.setPrice(actualPrice);
        actualProduct1.setDuration(actualDuration);
        productRepository.save(actualProduct1);

        actualProduct2 = Product.createNewProduct(
                null,
                "product2",
                "description",
                true);
        actualProduct2.setPrice(actualPrice);
        actualProduct2.setDuration(actualDuration);
        productRepository.save(actualProduct2);

    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(
                jdbcTemplate,
                "products",
                "prices",
                "durations");

    }

    @Test
    void findAllProductsByName() {
        //given

        String key = "summary";
        String value = "product2";

        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, value);
        ProductSpecification specification = new ProductSpecification(criteria);

        //when

        Product expected = productRepository.findAll(specification).getFirst();

        //then

        assertThat(expected).isNotNull();
        assertEquals(expected.getId(), actualProduct2.getId());
    }

    @Test
    void findAllProductsByActiveFalse() {
        //given

        String key = "active";
        Boolean active = false;
        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, String.valueOf(active));
        ProductSpecification specification = new ProductSpecification(criteria);

        //when

        Product expected = productRepository.findAll(specification).getFirst();

        //then

        assertThat(expected).isNotNull();
        assertEquals(expected.getId(), actualProduct1.getId());
    }

    @Test
    void findAllProductsByNameIfNameIsEmpty() {
        //given

        String key = "summary";
        String value = "";

        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, value);
        ProductSpecification specification = new ProductSpecification(criteria);

        //when

        List<Product> expected = productRepository.findAll(specification);

        //then

        assertThat(expected).isNotEmpty();
        assertThat(expected).hasSize(2);
    }

    @Test
    void findAllProductsIfNull() {
        //given

        String key = "summary";
        String value = null;

        SearchCriteria criteria = new SearchCriteria(
                key, OperationSpecification.EQUALS, value);
        ProductSpecification specification = new ProductSpecification(criteria);

        //when

        List<Product> expected = productRepository.findAll(specification);

        //then

        assertThat(expected).isEmpty();
    }

}
