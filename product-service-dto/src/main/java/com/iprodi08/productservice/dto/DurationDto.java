package com.iprodi08.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DurationDto {

    private Long id;

    @NotNull
    @Min(
            value = 1,
            message = "Duration of product in days must be greater or than equals to 1"
    )
    private Integer inDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DurationDto durationDto = (DurationDto) o;
        return Objects.equals(id, durationDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
