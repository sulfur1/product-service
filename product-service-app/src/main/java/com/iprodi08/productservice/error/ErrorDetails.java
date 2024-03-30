package com.iprodi08.productservice.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
    private Instant timestamp;
    private String message;
    private String details;
}
