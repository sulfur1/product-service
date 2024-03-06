package com.iprodi08.productservice.dto;

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

    private Integer inDays;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DurationDto that = (DurationDto) o;
        return Objects.equals(inDays, that.inDays);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(inDays);
    }
}
