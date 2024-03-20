package com.iprodi08.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    private String summary;

    private String description;

    private PriceDto priceDto;

    private DurationDto durationDto;

    private Boolean active;

    private List<DiscountDto> discountDtos;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductDto productDto = (ProductDto) o;
        return Objects.equals(id, productDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
