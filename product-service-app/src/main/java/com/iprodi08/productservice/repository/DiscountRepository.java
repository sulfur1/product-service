package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
