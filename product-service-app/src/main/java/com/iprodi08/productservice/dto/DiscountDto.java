package com.iprodi08.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {

    private Long id;

    private Integer value;

    private Instant dateTimeFrom;

    private Instant dateTimeUntil;

    private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscountDto discountDto = (DiscountDto) o;
        return Objects.equals(id, discountDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
