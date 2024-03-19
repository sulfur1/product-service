package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> getProductById(Long id);

    List<ProductDto> getAllProducts(Pageable pageable);

    List<ProductDto> getAllProductsByActive(Pageable pageable, Boolean active);

    List<ProductDto> getAllProductsByName(Pageable pageable, String name);

    Product createProduct(ProductDto productDto);

    void updatePriceOfProduct(Long productId, ProductDto productDto);

    void acivateOrDiactivateProduct(Long productId, Boolean active);

    void applyBulkDiscountToAllProducts(DiscountDto discountDto);

    void bulkActivateOrDiactivateDiscountForAllProducts(Long discountId, Boolean active);

    void activateOrDiactivateDiscountForProduct(Long discountId, Long productId, Boolean active);
}
