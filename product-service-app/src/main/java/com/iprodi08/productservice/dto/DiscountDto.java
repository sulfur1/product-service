package com.iprodi08.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {

    private Integer value;

    private OffsetDateTime dateTimeFrom;

    private OffsetDateTime dateTimeUntil;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscountDto discountDto = (DiscountDto) o;
        return Objects.equals(value, discountDto.value)
                && Objects.equals(dateTimeFrom, discountDto.dateTimeFrom)
                && Objects.equals(dateTimeUntil, discountDto.dateTimeUntil);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value, dateTimeFrom, dateTimeUntil);
    }
}
