package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.ProductDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> getProductById(long id);

    List<ProductDto> getAllProducts(Pageable pageable);

    List<ProductDto> getAllProductsByActive(Pageable pageable, boolean active);

    List<ProductDto> getAllProductsByName(Pageable pageable, String name);

    ProductDto createProduct(ProductDto productDto);

    Optional<ProductDto> updatePriceOfProduct(ProductDto productDto);

    void acivateProduct(long productId);

    void diactivateProduct(long productId);

    Optional<DiscountDto> applyDiscountToProduct(long productId, DiscountDto discountDto);

    Optional<DiscountDto> applyBulkDiscountToAllProducts(DiscountDto discountDto);

    void activateDiscountForAllProducts(long discountId);

    void diactivateDiscountForAllProducts(long discountId);

    void activateDiscountForProduct(long discountId, long productId);

    void diactivateDiscountForProduct(long discountId, long productId);
}
