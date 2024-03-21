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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.iprodi08.productservice.repository.filter.OperationSpecification.EQUALS;

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
    public Optional<ProductDto> getProductById(long productId) {
        return  productRepository
                .findById(productId)
                .map(mapper::productToProductDto);
    }

    @Override
    public List<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .stream()
                .map(mapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByActive(Pageable pageable, boolean active) {

        ProductSpecification productSpecification = new ProductSpecification(
                new SearchCriteria("active", EQUALS, String.valueOf(active))
        );

        return  productRepository
                .findAll(productSpecification, pageable)
                .stream()
                .map(mapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByName(Pageable pageable, String name) {
        ProductSpecification productSpecification = new ProductSpecification(
                new SearchCriteria("summary", EQUALS, name)
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
    public Optional<ProductDto> updatePriceOfProduct(ProductDto productDto) {
        return productRepository
                .findById(productDto.getId())
                .map(product -> {
                    PriceDto priceDto = productDto.getPriceDto();
                    Price price = Price.createNewPrice(null, priceDto.getValue(), priceDto.getCurrency());
                    return  mapper.productToProductDto(
                            productRepository.updatePriceForProductById(productDto.getId(), price)
                    );
                });
    }

    @Override
    @Transactional
    public void applyDiscountToProduct(long productId, DiscountDto discountDto) {
        productRepository.getProductByIdWithDiscounts(productId)
                .ifPresent(product -> {
                    Discount discount = Discount.createNewDiscount(
                            null,
                            discountDto.getValue(),
                            discountDto.getDateTimeFrom(),
                            discountDto.getDateTimeUntil(),
                            discountDto.getActive()
                    );
                    product.getDiscounts().add(discount);
                });
    }

    @Override
    @Transactional
    public void applyBulkDiscountToAllProducts(DiscountDto discountDto) {
        List<Product> products = productRepository.getAllProductsWithDiscounts();
        if (products.isEmpty()) {
            return;
        }

        Discount discount = Discount.createNewDiscount(
                null,
                discountDto.getValue(),
                discountDto.getDateTimeFrom(),
                discountDto.getDateTimeUntil(),
                discountDto.getActive()
        );
        products.forEach(product -> product.getDiscounts().add(discount));

    }

    @Override
    @Transactional
    public void activateDiscountForAllProducts(long discountId) {
        List<Product> products = productRepository.getAllProductsWithDiscountsById(discountId);
        if (!products.isEmpty()) {
            products.stream()
                    .flatMap(product -> product.getDiscounts().stream())
                    .forEach(discount -> discount.setActive(true));
        }

    }

    @Override
    @Transactional
    public void diactivateDiscountForAllProducts(long discountId) {
        List<Product> products = productRepository.getAllProductsWithDiscountsById(discountId);
        if (!products.isEmpty()) {
            products.stream()
                    .flatMap(product -> product.getDiscounts().stream())
                    .forEach(discount -> discount.setActive(false));
        }

    }

    @Override
    @Transactional
    public void activateDiscountForProduct(long discountId, long productId) {
        productRepository
                .getProductByIdWithDiscountById(productId, discountId)
                .ifPresent(product -> {
                    product.getDiscounts()
                            .getFirst()
                            .setActive(true);
                });
    }

    @Override
    @Transactional
    public void diactivateDiscountForProduct(long discountId, long productId) {
        productRepository
                .getProductByIdWithDiscountById(productId, discountId)
                .ifPresent(product -> {
                    product.getDiscounts()
                            .getFirst()
                            .setActive(false);
                });
    }


    @Override
    public void acivateProduct(long productId) {
        productRepository
                .findById(productId)
                .ifPresent(updateProduct -> productRepository.updateActiveOfProductById(productId, true));
    }

    @Override
    public void diactivateProduct(long productId) {
        productRepository
                .findById(productId)
                .ifPresent(updateProduct -> productRepository.updateActiveOfProductById(productId, false));
    }

}
