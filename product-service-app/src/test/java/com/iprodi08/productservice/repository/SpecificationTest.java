package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.AbstractTest;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.filter.OperationSpecification;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.repository.filter.SearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext
class SpecificationTest extends AbstractTest {

    @Test
    void findAllProductsByName() {
        //given

        String key = "summary";
        String value = "Product2";

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
