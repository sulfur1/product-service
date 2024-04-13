package com.iprodi08.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprodi08.productservice.AbstractTest;
import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.DurationDto;
import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import com.iprodi08.productservice.test_data.ProductTestData;
import com.iprodi08.productservice.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_1_ACTIVE_OFFSET_1;
import static com.iprodi08.productservice.test_data.DiscountTestData.DISCOUNT_1_DATE_TIME_UNTIL;
import static com.iprodi08.productservice.test_data.DiscountTestData.TIME_ZONE;
import static com.iprodi08.productservice.test_data.DiscountTestData.getDiscountDto;
import static com.iprodi08.productservice.test_data.DiscountTestData.getNewDiscount1;
import static com.iprodi08.productservice.test_data.DurationTestData.getNewDuration;
import static com.iprodi08.productservice.test_data.PriceTestData.getNewPrice;
import static com.iprodi08.productservice.test_data.PriceTestData.getPriceDto;
import static com.iprodi08.productservice.test_data.ProductTestData.getProductDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext
class ITProductController extends AbstractTest {

    private static final String BASE_URL = "/api";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createProduct() throws Exception {
        //given
        Price price = getNewPrice();
        PriceDto actualPriceDto = getPriceDto(price);
        Duration duration = getNewDuration();
        Discount discount = getNewDiscount1();
        Product createProduct = Product.createNewProduct(
                null,
                "NewProduct",
                "This is new product",
                true
        );
        createProduct.setPrice(price);
        createProduct.setDuration(duration);
        createProduct.setDiscounts(List.of(discount));
        String requestJson = JsonUtil.writeValue(getProductDto(createProduct));
        //when

        MvcResult result = mvc.perform(
                post(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andDo(print())

        //then

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ProductDto productDto = JsonUtil.readValue(responseJson, ProductDto.class);
        assertEquals(productDto.getSummary(), createProduct.getSummary());
        assertEquals(productDto.getDescription(), createProduct.getDescription());
        assertEquals(productDto.getActive(), createProduct.getActive());

        PriceDto priceDto = productDto.getPriceDto();
        assertEquals(priceDto.getValue(), actualPriceDto.getValue());
        assertEquals(priceDto.getCurrency(), actualPriceDto.getCurrency());

        DurationDto durationDto = productDto.getDurationDto();
        assertEquals(durationDto.getInDays(), duration.getInDays());

        DiscountDto discountDto = productDto.getDiscountDtos().getFirst();
        assertEquals(discountDto.getValue(), discount.getValue());
        assertEquals(discountDto.getActive(), discount.getActive());
        assertEquals(discountDto.getDateTimeFrom(), discount.getDateTimeFrom());
        assertEquals(discountDto.getDateTimeUntil(), discount.getDateTimeUntil());
    }

    @Test
    void createDuplicateProduct() throws Exception {
        //given
        Price price = getNewPrice();
        Duration duration = getNewDuration();
        Discount discount = getNewDiscount1();
        Product createProduct = Product.createNewProduct(
                null,
                "Product1",
                "This is product1",
                true
        );
        createProduct.setPrice(price);
        createProduct.setDuration(duration);
        createProduct.setDiscounts(List.of(discount));
        String requestJson = JsonUtil.writeValue(getProductDto(createProduct));
        //when

        mvc.perform(
                        post(BASE_URL + "/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andDo(print())

                //then

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();
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
        Price updatedPrice = PriceTestData.getNewPrice1();
        updatedPrice.setValue(priceValue);
        actualProduct1.setPrice(updatedPrice);
        ProductDto productDto = getProductDto(actualProduct1);
        String requestJson = JsonUtil.writeValue(productDto);

        //when

        MvcResult result = mvc.perform(put(BASE_URL + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

        //then
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(requestJson, result.getRequest().getContentAsString());
    }

    @Test
    void applyDiscountToProduct() throws Exception {
        //given

        Integer discountValue = 10;
        Discount discount = DiscountTestData.getNewDiscount1();
        discount.setValue(discountValue);
        DiscountDto actual = getDiscountDto(discount);
        String requestJson = JsonUtil.writeValue(actual);

        //when

        MvcResult result = mvc.perform(put(
                BASE_URL + "/products/{productId}/id/apply_discount",
                        actualProduct2.getId()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())

        //then
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        DiscountDto discountDto = JsonUtil.readValue(responseJson, DiscountDto.class);
        actual.setId(discountDto.getId());
        assertEquals(actual, discountDto);
        Optional<Product> productWithDiscounts = productRepository
                .getProductByIdWithDiscounts(actualProduct2.getId());
        assertThat(productWithDiscounts.get().getDiscounts()).hasSize(3);
    }

    @Test
    void applyBulkDiscountForAllProducts() throws Exception {
        //given

        Integer discountValue = 10;
        Discount discount = DiscountTestData.getNewDiscount1();
        discount.setValue(discountValue);
        DiscountDto actual = getDiscountDto(discount);
        String requestJson = JsonUtil.writeValue(actual);

        //when

        MvcResult result = mvc.perform(put(
                        BASE_URL + "/products/all/apply_discount",
                        actualDiscount2.getId()
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())

        //then
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        DiscountDto discountDto = JsonUtil.readValue(responseJson, DiscountDto.class);
        actual.setId(discountDto.getId());
        assertEquals(actual, discountDto);
        List<Product> products = productRepository.getAllProductsWithDiscounts();
        List<Discount> discounts = products.stream()
                .flatMap(product -> product.getDiscounts().stream())
                .toList();
        assertThat(discounts).hasSize(6);
    }

    @Test
    @Transactional
    void changeActiveToFalseDiscountForAllProducts() throws Exception {
        //given

        boolean active = false;
        Integer discountNewValue = 7;
        Discount discount = Discount.createNewDiscount(
                null,
                discountNewValue,
                LocalDateTime
                        .now()
                        .plusHours(DISCOUNT_1_ACTIVE_OFFSET_1)
                        .toInstant(ZoneOffset.of(TIME_ZONE)),
                DISCOUNT_1_DATE_TIME_UNTIL,
                true
        );
        Discount created = discountRepository.save(discount);
        productRepository.getAllProductsWithDiscounts()
                                .forEach(
                                        product -> product.getDiscounts().add(created)
                                );

        //when

        mvc.perform(
                patch(
                BASE_URL + "/products/all/discounts/{discountId}/id/change_active",
                created.getId()
                )
                .param("active", String.valueOf(active))
        )
                .andDo(print())

        //then
                .andExpect(status().isAccepted());

        List<Discount> expected = productRepository.getAllProductsWithDiscounts()
                .stream()
                .flatMap(product -> product.getDiscounts().stream())
                .filter(discount1 -> discount1.getId().equals(created.getId()))
                .toList();

        for (Discount d : expected) {
            assertEquals(d.getId(), created.getId());
            assertEquals(d.getActive(), active);
        }
    }

    @Test
    void changeActiveToFalseDiscountForProduct() throws Exception {
        //given

        boolean active = false;

        //when

        mvc.perform(patch(
                BASE_URL + "/products/{productId}/id/discounts/{discountId}/id/change_active",
                actualProduct1.getId(),
                actualDiscount1.getId())
                .param("active", String.valueOf(active))
        )
                .andDo(print())

        //then

                .andExpect(status().isAccepted());

        Optional<Product> product = productRepository.getProductByIdWithDiscounts(actualProduct1.getId());
        assertThat(product).isNotEmpty();
        List<Discount> discounts = product.get().getDiscounts()
                .stream()
                .filter(Discount::getActive)
                .toList();
        assertThat(discounts).hasSize(1);
        assertEquals(discounts.getFirst().getId(), actualDiscount2.getId());
    }

    @Test
    void getAllProducts() throws Exception {
        //given

        List<ProductDto> productDtos = Stream.of(actualProduct1, actualProduct2)
                .map(ProductTestData::getProductDto)
                .toList();

        //when

        MvcResult result = mvc.perform(get(BASE_URL + "/products/all"))
                .andDo(print())

        //then

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ProductDto> expected = JsonUtil.readValues(jsonResponse, ProductDto.class);

        assertThat(expected).isNotEmpty();
        assertThat(expected).hasSize(2);
        assertEquals(expected, productDtos);
    }

    @Test
    void getAllProductsBySortDiscount() throws Exception {
        //given
        List<ProductDto> actualProductDtos = Stream.of(actualProduct2, actualProduct1)
                .map(ProductTestData::getProductDto)
                .toList();

        //when

        MvcResult result = mvc.perform(get(
                        BASE_URL + "/products/all/sort-discount"))
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        List<ProductDto> expectedProductDtos = JsonUtil.readValues(responseJson, ProductDto.class);
        assertThat(expectedProductDtos).isNotEmpty();
        assertThat(expectedProductDtos).hasSize(2);
        for (int i = 0; i < actualProductDtos.size(); i++) {
            assertEquals(expectedProductDtos.get(i), actualProductDtos.get(i));
        }
    }

    @Test
    void getAllProductsBySortPrice() throws Exception {
        //given
        List<ProductDto> actualProductDtos = Stream.of(actualProduct1, actualProduct2)
                .map(ProductTestData::getProductDto)
                .toList();

        //when

        MvcResult result = mvc.perform(get(
                        BASE_URL + "/products/all/sort-price"))
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        List<ProductDto> expectedProductDtos = JsonUtil.readValues(responseJson, ProductDto.class);
        assertThat(expectedProductDtos).isNotEmpty();
        assertThat(expectedProductDtos).hasSize(2);
        for (int i = 0; i < actualProductDtos.size(); i++) {
            assertEquals(expectedProductDtos.get(i), actualProductDtos.get(i));
        }
    }

    @Test
    void getAllProductsWithName() throws Exception {
        //given
        List<ProductDto> actualProductDtos = Stream.of(actualProduct1)
                .map(ProductTestData::getProductDto)
                .toList();

        String name = "Product1";

        //when

        MvcResult result = mvc.perform(get(
                BASE_URL + "/products/all/by-name")
                .param("name", name))
                .andDo(print())

        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        List<ProductDto> expectedProductDtos = JsonUtil.readValues(responseJson, ProductDto.class);
        assertThat(expectedProductDtos).isNotEmpty();
        assertThat(expectedProductDtos).hasSize(1);
        assertEquals(expectedProductDtos, actualProductDtos);
    }

    @Test
    void getAllProductsWithActive() throws Exception {
        //given
        List<ProductDto> actualProductDtos = Stream.of(actualProduct1)
                .map(ProductTestData::getProductDto)
                .toList();

        boolean active = false;

        //when

        MvcResult result = mvc.perform(get(
                        BASE_URL + "/products/all/by-active")
                        .param("active", String.valueOf(active)))
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        List<ProductDto> expectedProductDtos = JsonUtil.readValues(responseJson, ProductDto.class);
        assertThat(expectedProductDtos).isNotEmpty();
        assertThat(expectedProductDtos).hasSize(1);
        assertEquals(expectedProductDtos, actualProductDtos);
    }

    @Test
    void getProductById() throws Exception {
        //given

        ProductDto actual = getProductDto(actualProduct2);

        //when

        MvcResult result = mvc.perform(get(
                BASE_URL + "/products/{productId}",
                actualProduct2.getId()))
                .andDo(print())

        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ProductDto expected = JsonUtil.readValue(responseJson, ProductDto.class);
        assertEquals(expected, actual);
    }
}
