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

    private Long id;

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
