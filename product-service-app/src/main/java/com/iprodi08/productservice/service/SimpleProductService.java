package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.PriceDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.dto.mapper.ProductMapper;
import com.iprodi08.productservice.entity.Discount;
import com.iprodi08.productservice.entity.Duration;
import com.iprodi08.productservice.entity.Price;
import com.iprodi08.productservice.entity.Product;
import com.iprodi08.productservice.repository.DiscountRepository;
import com.iprodi08.productservice.repository.DurationRepository;
import com.iprodi08.productservice.repository.PriceRepository;
import com.iprodi08.productservice.repository.ProductRepository;
import com.iprodi08.productservice.repository.filter.ProductSpecification;
import com.iprodi08.productservice.repository.filter.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.iprodi08.productservice.repository.filter.OperationSpecification.EQUALS;

@Service
public class SimpleProductService implements ProductService {

    private final ProductMapper mapper;

    private final ProductRepository productRepository;

    private final PriceRepository priceRepository;

    private final DurationRepository durationRepository;

    private final DiscountRepository discountRepository;

    @Autowired
    public SimpleProductService(ProductMapper mapper,
                                ProductRepository productRepository,
                                PriceRepository priceRepository,
                                DurationRepository durationRepository,
                                DiscountRepository discountRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.durationRepository = durationRepository;
        this.discountRepository = discountRepository;
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
                .toList();
    }

    @Override
    public List<ProductDto> getAllProductsByActive(Pageable pageable, boolean active) {

        ProductSpecification productSpecification = new ProductSpecification(
                new SearchCriteria("active", EQUALS, String.valueOf(active))
        );

        return  productRepository
                .findAll(productSpecification, pageable)
                .getContent()
                .stream()
                .map(mapper::productToProductDto)
                .toList();
    }

    @Override
    public List<ProductDto> getAllProductsByName(Pageable pageable, String name) {
        ProductSpecification productSpecification = new ProductSpecification(
                new SearchCriteria("summary", EQUALS, name)
        );

        return  productRepository
                .findAll(productSpecification, pageable)
                .getContent()
                .stream()
                .map(mapper::productToProductDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductDto createProduct(final ProductDto productDto) {
        Product create = mapper.productDtoToProduct(productDto);
        Price price = priceRepository.save(create.getPrice());
        Duration duration = durationRepository.save(create.getDuration());
        List<Discount> discounts = discountRepository.saveAll(create.getDiscounts());
        create.setPrice(price);
        create.setDuration(duration);
        create.setDiscounts(discounts);
        return mapper.productToProductDto(productRepository.save(create));
    }

    @Override
    @Transactional
    public Optional<ProductDto> updatePriceOfProduct(ProductDto productDto) {
        Long productId = productDto.getId();
        productRepository
                .findById(productId)
                .ifPresent(product -> {
                    PriceDto priceDto = productDto.getPriceDto();
                    Price price = priceRepository.save(
                            Price.createNewPrice(null, priceDto.getValue(), priceDto.getCurrency())
                    );
                    productRepository.updatePriceForProductById(productId, price);
                });
        return getProductById(productId);
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

                    product.getDiscounts().add(discountRepository.save(discount));
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
        products.forEach(product -> product.getDiscounts().add(discountRepository.save(discount)));

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
