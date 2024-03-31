package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> getProductById(long id);

    List<ProductDto> getAllProducts(Pageable pageable);

    List<ProductDto> getAllProductsByActive(Pageable pageable, boolean active);

    List<ProductDto> getAllProductsByName(Pageable pageable, String name);

    Product createProduct(ProductDto productDto);

    Optional<ProductDto> updatePriceOfProduct(ProductDto productDto);

    void acivateProduct(long productId);

    void diactivateProduct(long productId);

    void applyDiscountToProduct(long productId, DiscountDto discountDto);

    void applyBulkDiscountToAllProducts(DiscountDto discountDto);

    void activateDiscountForAllProducts(long discountId);

    void diactivateDiscountForAllProducts(long discountId);

    void activateDiscountForProduct(long discountId, long productId);

    void diactivateDiscountForProduct(long discountId, long productId);
}
