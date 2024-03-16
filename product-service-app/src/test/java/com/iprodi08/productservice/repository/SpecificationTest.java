package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.entity.enumType.Currency;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.repository.filter.SearchCriteria;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql("/sql_scripts/create-tables-test.sql")
@Sql(scripts = "/sql_scripts/cleanup-tables-test.sql", executionPhase = AFTER_TEST_METHOD)
class SpecificationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private DurationRepository durationRepository;

    @Test
    void findAllProductsByName() {
        SearchCriteria criteria = new SearchCriteria("summary", ":", "product2");
        ProductSpecification specification = new ProductSpecification(criteria);

        Price actualPrice1 = new Price(BigDecimal.valueOf(123.54), Currency.USD);
        Price expectedPrice1 = priceRepository.save(actualPrice1);

        Duration actualDuration1 = new Duration(60);
        Duration expectedDuration1 = durationRepository.save(actualDuration1);

        Price actualPrice2 = new Price(BigDecimal.valueOf(500.54), Currency.USD);
        Price expectedPrice2 = priceRepository.save(actualPrice2);

        Duration actualDuration2 = new Duration(90);
        Duration expectedDuration2 = durationRepository.save(actualDuration2);

        Product actualProduct1 = new Product("product1", "description", expectedPrice1, expectedDuration1, true);
        productRepository.save(actualProduct1);

        Product actualProduct2 = new Product("product2", "description", expectedPrice2, expectedDuration2, true);
        productRepository.save(actualProduct2);

        Product expected = productRepository.findAll(specification).getFirst();

        assertEquals(actualProduct2.getSummary(), expected.getSummary());
        assertEquals(actualProduct2.getDescription(), expected.getDescription());
        assertEquals(actualProduct2.getCreatedAt(), expected.getCreatedAt());
        assertEquals(actualProduct2.getUpdatedAt(), expected.getUpdatedAt());
        assertSame(actualProduct2.getActive(), expected.getActive());

    }

    @Test
    @Transactional
    void findAllProductsByActiveFalse() {
        SearchCriteria criteria = new SearchCriteria("active", ":", "false");
        ProductSpecification specification = new ProductSpecification(criteria);

        Price actualPrice1 = new Price(BigDecimal.valueOf(123.54), Currency.USD);
        Price expectedPrice1 = priceRepository.save(actualPrice1);

        Duration actualDuration1 = new Duration(60);
        Duration expectedDuration1 = durationRepository.save(actualDuration1);

        Price actualPrice2 = new Price(BigDecimal.valueOf(500.54), Currency.USD);
        Price expectedPrice2 = priceRepository.save(actualPrice2);

        Duration actualDuration2 = new Duration(90);
        Duration expectedDuration2 = durationRepository.save(actualDuration2);

        Product actualProduct1 = new Product("product1", "description", expectedPrice1, expectedDuration1, true);
        productRepository.save(actualProduct1);

        Product actualProduct2 = new Product("product2", "description", expectedPrice2, expectedDuration2, false);
        productRepository.save(actualProduct2);

        Product expected = productRepository.findAll(specification).getFirst();


        assertEquals(actualProduct2.getSummary(), expected.getSummary());
        assertEquals(actualProduct2.getDescription(), expected.getDescription());
        assertEquals(actualProduct2.getCreatedAt(), expected.getCreatedAt());
        assertEquals(actualProduct2.getUpdatedAt(), expected.getUpdatedAt());
        assertSame(actualProduct2.getActive(), expected.getActive());

    }

}
