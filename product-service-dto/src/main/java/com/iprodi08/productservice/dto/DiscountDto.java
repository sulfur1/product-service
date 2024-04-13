package com.iprodi08.productservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {

    private Long id;

    @NotNull(message = "For discount value require non null")
    @Range(
            max = 100,
            message = """
                    Discount value must be:
                    greater than or equal to sign 0 and less than or equal to sign 100
                    """)
    private Integer value;

    @NotNull
    @FutureOrPresent
    private Instant dateTimeFrom;

    @NotNull
    @Future(
            message = "Discount finish date and time value is in the future"
    )
    private Instant dateTimeUntil;

    @NotNull
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
