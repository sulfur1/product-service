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

    private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscountDto discountDto = (DiscountDto) o;
        return Objects.equals(value, discountDto.value)
                && Objects.equals(dateTimeFrom, discountDto.dateTimeFrom)
                && Objects.equals(dateTimeUntil, discountDto.dateTimeUntil)
                && Objects.equals(active, discountDto.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, dateTimeFrom, dateTimeUntil, active);
    }
}
