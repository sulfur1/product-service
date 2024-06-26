package com.iprodi08.productservice.repository.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private OperationSpecification operation;
    private String value;
}
