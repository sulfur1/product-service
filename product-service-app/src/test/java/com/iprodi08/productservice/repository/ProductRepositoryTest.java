package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.AbstractTest;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.test_data.PriceTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext
class ProductRepositoryTest extends AbstractTest {

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
        assertThat(expected).hasSize(1);
        assertThat(expectedDiscount).isNotEmpty();
        assertThat(expectedDiscount).hasSize(1);
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
    void updatePriceForProductById() {
        //given

        Price newPrice = priceRepository.save(PriceTestData.getNew());

        //when

        productRepository.updatePriceForProductById(actualProduct2.getId(), newPrice);

        //then

        Optional<Product> updatedProduct = productRepository.findById(actualProduct2.getId());
        assertThat(updatedProduct).isNotEmpty();
        assertNotEquals(updatedProduct.get().getPrice().getId(), actualPrice.getId());
        assertEquals(updatedProduct.get().getPrice().getId(), newPrice.getId());
    }

    @Test
    void updateActiveOfProductById() {
        //given

        Boolean active = false;

        //when

        productRepository.updateActiveOfProductById(actualProduct2.getId(), active);

        //then

        Optional<Product> updatedProduct = productRepository.findById(actualProduct2.getId());
        assertThat(updatedProduct).isNotEmpty();
        assertFalse(updatedProduct.get().getActive());
    }
}
