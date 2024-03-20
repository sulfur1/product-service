package com.iprodi08.productservice.dto;

import com.iprodi08.productservice.entity.enumType.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    private Long id;

    private BigDecimal value;

    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceDto priceDto = (PriceDto) o;
        return Objects.equals(id, priceDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
