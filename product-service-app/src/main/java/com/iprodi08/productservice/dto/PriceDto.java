package com.iprodi08.productservice.dto;

import com.iprodi08.productservice.entity.enumType.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 5, fraction = 2)
    private BigDecimal value;

    @NotNull
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
