package com.iprodi08.productservice.dto;

import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.entity.enumType.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class ProductTestData {

    private static final Integer IN_DAYS = 60;
    private static final Integer DISCOUNT = 60;

    private ProductTestData() {

    }
    public static Price getPrice() {
        final Price price = new Price();
        price.setValue(BigDecimal.TEN);
        price.setCurrency(Currency.RUB);
        return price;
    }
    public static PriceDto getPriceDto() {
        final PriceDto priceDto = new PriceDto();
        priceDto.setValue(BigDecimal.TEN);
        priceDto.setCurrency(Currency.RUB);
        return priceDto;
    }

    public static Duration getDuration() {
        final Duration duration = new Duration();
        duration.setInDays(IN_DAYS);
        return duration;
    }
    public static DurationDto getDurationDto() {
        final DurationDto durationDto = new DurationDto();
        durationDto.setInDays(IN_DAYS);
        return durationDto;
    }

    public static Discount getDiscount() {
        final Discount discount = new Discount();
        discount.setValue(DISCOUNT);
        discount.setDateTimeFrom(OffsetDateTime.now().minusHours(1L));
        discount.setDateTimeUntil(OffsetDateTime.now().plusHours(2L));
        return discount;
    }

    public static DiscountDto getDiscountDto() {
        final DiscountDto discountDto = new DiscountDto();
        discountDto.setValue(DISCOUNT);
        discountDto.setDateTimeFrom(OffsetDateTime.now().minusHours(1L));
        discountDto.setDateTimeUntil(OffsetDateTime.now().plusHours(2L));
        return discountDto;
    }

    public static Product getProduct() {
        final Product product = new Product();
        product.setSummary("Product1");
        product.setDescription("This is product 1");
        product.setPrice(getPrice());
        product.setDurations(List.of(getDuration()));
        product.setActive(true);
        product.setDiscounts(List.of(getDiscount()));
        return product;
    }
}
