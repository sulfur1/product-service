package com.iprodi08.productservice.repository;

import com.iprodi08.productservice.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
