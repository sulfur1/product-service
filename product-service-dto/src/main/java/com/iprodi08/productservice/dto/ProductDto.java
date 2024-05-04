package com.iprodi08.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "For summary require non null")
    private String summary;

    @NotBlank(message = "For summary require non null")
    private String description;

    @NotNull
    private PriceDto priceDto;

    @NotNull
    private DurationDto durationDto;

    @NotNull
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
