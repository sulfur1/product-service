package com.iprodi08.productservice.controller;

import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.DiscountRepository;
import com.iprodi08.productservice.repository.DurationRepository;
import com.iprodi08.productservice.repository.PriceRepository;
import com.iprodi08.productservice.repository.ProductRepository;
import com.iprodi08.productservice.test_data.DiscountTestData;
import com.iprodi08.productservice.test_data.DurationTestData;
import com.iprodi08.productservice.test_data.PriceTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AbstractTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected PriceRepository priceRepository;

    @Autowired
    protected DurationRepository durationRepository;

    @Autowired
    protected DiscountRepository discountRepository;

    protected Product actualProduct1;

    protected Product actualProduct2;

    protected Price actualPrice1;

    protected Price actualPrice2;

    protected Duration actualDuration;

    protected Discount actualDiscount1;

    protected Discount actualDiscount2;

    protected Discount actualDiscount3;

    protected Discount actualDiscount4;

    @BeforeEach
    void setUp() {
        actualPrice1 = priceRepository.save(PriceTestData.getNewPrice1());
        actualPrice2 = priceRepository.save(PriceTestData.getNewPrice2());
        actualDuration = durationRepository.save(DurationTestData.getNewDuration());
        actualDiscount1 = discountRepository.save(DiscountTestData.getNewDiscount1());
        actualDiscount2 = discountRepository.save(DiscountTestData.getNewDiscount2());
        actualProduct1 = Product.createNewProduct(
                null,
                "Product1",
                "This is product1",
                false
        );
        /*Discount discount1 = DiscountTestData.getNewDiscount1();
        Discount discount2 = DiscountTestData.getNewDiscount2();*/
        List<Discount> discountsOfProduct1 = new ArrayList<>();
        discountsOfProduct1.add(actualDiscount1);
        discountsOfProduct1.add(actualDiscount2);

        actualProduct1.setPrice(actualPrice1);
        actualProduct1.setDuration(actualDuration);
        actualProduct1.setDiscounts(discountsOfProduct1);
        actualProduct1 = productRepository.save(actualProduct1);
        /*actualDiscount1 = actualProduct1.getDiscounts().getFirst();
        actualDiscount2 = actualProduct1.getDiscounts().getLast();*/


        actualProduct2 = Product.createNewProduct(
                null,
                "Product2",
                "This is product2",
                true);
        actualDiscount3 = discountRepository.save(DiscountTestData.getNewDiscount3());
        actualDiscount4 = discountRepository.save(DiscountTestData.getNewDiscount4());
        /*Discount discount3 = DiscountTestData.getNewDiscount3();
        Discount discount4 = DiscountTestData.getNewDiscount4();*/
        List<Discount> discountsOfProduct2 = new ArrayList<>();
        discountsOfProduct2.add(actualDiscount3);
        discountsOfProduct2.add(actualDiscount4);

        actualProduct2.setPrice(actualPrice2);
        actualProduct2.setDuration(actualDuration);
        actualProduct2.setDiscounts(discountsOfProduct2);
        actualProduct2 = productRepository.save(actualProduct2);
        /*actualDiscount3 = actualProduct2.getDiscounts().getFirst();
        actualDiscount4 = actualProduct2.getDiscounts().getLast();*/

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
}
