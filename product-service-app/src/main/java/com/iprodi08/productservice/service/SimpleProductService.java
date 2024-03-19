package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.ProductRepository;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.repository.filter.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleProductService implements ProductService {

    private final ProductMapper mapper;

    private final ProductRepository productRepository;

    @Autowired
    public SimpleProductService(final ProductMapper mapper,
                                final ProductRepository productRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductDto> getProductById(final Long prosuctId) {
        Objects.requireNonNull(prosuctId, "prosuctId require non null");

        return  productRepository
                .findById(prosuctId)
                .map(mapper::productToProductDto);
    }

    @Override
    public List<ProductDto> getAllProducts(final Pageable pageable) {
        return productRepository.findAll(pageable)
                .stream()
                .map(mapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByActive(final Pageable pageable, final Boolean active) {
        Objects.requireNonNull(active, "Active require non null");

        ProductSpecification productSpecification = new ProductSpecification(
                new SearchCriteria("active", ":", String.valueOf(active))
        );

        return  productRepository
                .findAll(productSpecification, pageable)
                .stream()
                .map(mapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByName(final Pageable pageable, final String name) {
        ProductSpecification productSpecification = new ProductSpecification(
                new SearchCriteria("summary", ":", name)
        );

        return  productRepository
                .findAll(productSpecification, pageable)
                .stream()
                .map(mapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Product createProduct(final ProductDto productDto) {
        return productRepository.save(mapper.productDtoToProduct(productDto));
    }

    @Override
    @Transactional
    public void updatePriceOfProduct(final Long productId, final ProductDto productDto) {
        Objects.requireNonNull(productId, "productId require non null");

        Product updateProduct = productRepository.findById(productId).orElse(null);
        if (updateProduct != null) {
            PriceDto priceDto = productDto.getPriceDto();
            Price price = new Price(priceDto.getValue(), priceDto.getCurrency());
            updateProduct.setPrice(price);
        }
    }

    @Override
    @Transactional
    public void applyBulkDiscountToAllProducts(final DiscountDto discountDto) {
        List<Product> products = productRepository.getAllProductsWithDiscounts();
        if (!products.isEmpty()) {
            Discount discount = new Discount(
                    discountDto.getValue(),
                    discountDto.getDateTimeFrom(),
                    discountDto.getDateTimeUntil(),
                    discountDto.getActive()
            );
            products.forEach(product -> product.getDiscounts().add(discount));
        }
    }

    @Override
    @Transactional
    public void bulkActivateOrDiactivateDiscountForAllProducts(Long discountId, Boolean active) {
        List<Product> products = productRepository.getAllProductsWithDiscountsById(discountId);
        if (!products.isEmpty()) {
            products.forEach(product -> product.setActive(active));
        }

    }

    @Override
    @Transactional
    public void activateOrDiactivateDiscountForProduct(Long discountId, Long productId, Boolean active) {
        productRepository
                .getProductByIdWithDiscounts(productId, discountId)
                .ifPresent(product -> product.setActive(active));
    }


    @Override
    @Transactional
    public void acivateOrDiactivateProduct(Long productId, Boolean active) {
        Objects.requireNonNull(productId, "productId require non null");

        productRepository
                .findById(productId)
                .ifPresent(updateProduct -> updateProduct.setActive(active));
    }


}
