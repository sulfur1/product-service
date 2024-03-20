package com.iprodi08.productservice.repository.filter;

import com.iprodi08.productservice.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification implements Specification<Product> {

    private final SearchCriteria criteria;

    public ProductSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            @NonNull Root<Product> root,
            @NonNull CriteriaQuery<?> query,
            @NonNull CriteriaBuilder builder) {
        Predicate result = null;
        if (criteria.getOperation() == OperationSpecification.MORE) {
            result = builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue()
            );
        }
        else if (criteria.getOperation() == OperationSpecification.LESS) {
            result = builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue()
            );
        }
        else if (criteria.getOperation() == OperationSpecification.EQUALS) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                result = builder.like(
                        root.get(criteria.getKey()), String.format("%%%s%%", criteria.getValue()));
            } else {
                final Boolean value = Boolean.valueOf(criteria.getValue());
                result = builder.equal(root.get(criteria.getKey()), value);
            }
        }
        return result;
    }
}
