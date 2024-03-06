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

    private String summary;

    private String description;

    private PriceDto priceDto;

    private List<DurationDto> durationDtos;

    private Boolean active;

    private List<DiscountDto> discountDtos;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductDto productDto = (ProductDto) o;
        return Objects.equals(summary, productDto.summary)
                && Objects.equals(description, productDto.description)
                && Objects.equals(priceDto, productDto.priceDto)
                && Objects.equals(durationDtos, productDto.durationDtos)
                && Objects.equals(active, productDto.active)
                && Objects.equals(discountDtos, productDto.discountDtos);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(summary, description, priceDto, durationDtos, active, discountDtos);
    }
}
