package com.iprodi08.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprodi08.productservice.AbstractTest;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.iprodi08.productservice.test_data.DiscountTestData.getDiscountDto;
import static com.iprodi08.productservice.test_data.ProductTestData.getProductDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext
class ProductControllerTest extends AbstractTest {

    private static final String BASE_URL = "/api";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createProduct() {
    }

    @Test
    void changeActiveProductOnFalse() throws Exception {
        //given

        boolean active = false;
        Long productId = actualProduct1.getId();

        //when

         mvc.perform(patch(
                         BASE_URL + "/products/{productId}/change_active",
                         productId)
                         .param("active", String.valueOf(active))
                 )
                 .andDo(print())
        //then
                 .andExpect(status().isNoContent());

        Optional<Product> product = productRepository.findById(productId);
        assertThat(product).isNotEmpty();
        assertEquals(product.get().getActive(), active);

    }

    @Test
    void updatePriceOfProduct() throws Exception {
        //given

        BigDecimal priceValue = BigDecimal.valueOf(123.00);
        Price updatedPrice = PriceTestData.getNew();
        updatedPrice.setValue(priceValue);
        actualProduct1.setPrice(updatedPrice);
        ProductDto productDto = getProductDto(actualProduct1);
        String requestBody = mapper.writeValueAsString(productDto);

        //when

        MvcResult result = mvc.perform(put(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())

        //then
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(requestBody, result.getRequest().getContentAsString());
    }

    @Test
    void applyDiscountToProduct() throws Exception {
        //given

        Integer discountValue = 10;
        Discount discount = DiscountTestData.getNew();
        discount.setValue(discountValue);
        String request = mapper.writeValueAsString(getDiscountDto(discount));

        //when

        mvc.perform(put(
                BASE_URL + "/products/{productId}/id/apply_discount",
                        actualProduct2.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andDo(print())

        //then
                .andExpect(status().isAccepted());

        Optional<Product> product = productRepository.getProductByIdWithDiscounts(actualProduct2.getId());
        assertThat(product.get().getDiscounts()).hasSize(3);
    }

    @Test
    void applyBulkDiscountForAllProducts() throws Exception {
        //given

        Integer discountValue = 10;
        Discount discount = DiscountTestData.getNew();
        discount.setValue(discountValue);
        String request = mapper.writeValueAsString(getDiscountDto(discount));

        //when

        mvc.perform(put(
                        BASE_URL + "/products/all/apply_discount",
                        actualDiscount2.getId()
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())

        //then
                .andExpect(status().isAccepted());

        List<Product> products = productRepository.getAllProductsWithDiscounts();
        List<Discount> discounts = products.stream()
                .flatMap(product -> product.getDiscounts().stream())
                .toList();
        assertThat(discounts).hasSize(6);
    }

    @Test
    void changeActiveDiscountForAllProducts() {
    }

    @Test
    void changeActiveDiscountForProducts() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getAllProductsBySortDiscount() {
    }

    @Test
    void getAllProductsBySortPrice() {
    }

    @Test
    void getAllProductsWithName() {
    }

    @Test
    void getAllProductsWithActive() {
    }

    @Test
    void getProductById() {
    }
}
