package com.iprodi08.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    @NotBlank
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Currency must contain 3 capital characters In the range A-Z"
    )
    private String currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrencyDto currencyDto = (CurrencyDto) o;
        return Objects.equals(currency, currencyDto.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency);
    }
}
