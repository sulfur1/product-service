package com.iprodi08.productservice.service;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.mapper.DiscountMapper;
import com.iprodi08.productservice.mapper.PriceMapper;
import com.iprodi08.productservice.mapper.ProductMapper;
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

    private final ProductMapper productMapper;

    private final DiscountMapper discountMapper;

    private final PriceMapper priceMapper;

    private final ProductRepository productRepository;

    private final PriceRepository priceRepository;

    private final DurationRepository durationRepository;

    private final DiscountRepository discountRepository;

    @Autowired
    public SimpleProductService(ProductMapper productMapper,
                                DiscountMapper discountMapper,
                                PriceMapper priceMapper,
                                ProductRepository productRepository,
                                PriceRepository priceRepository,
                                DurationRepository durationRepository,
                                DiscountRepository discountRepository) {
        this.productMapper = productMapper;
        this.discountMapper = discountMapper;
        this.priceMapper = priceMapper;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.durationRepository = durationRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public Optional<ProductDto> getProductById(long productId) {
        return  productRepository
                .findById(productId)
                .map(productMapper::productToProductDto);
    }

    @Override
    public List<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .stream()
                .map(productMapper::productToProductDto)
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
                .map(productMapper::productToProductDto)
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
                .map(productMapper::productToProductDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductDto createProduct(final ProductDto productDto) {
        Product create = productMapper.productDtoToProduct(productDto);
        Price price = priceRepository.save(create.getPrice());
        Duration duration = durationRepository.save(create.getDuration());
        List<Discount> discounts = discountRepository.saveAll(create.getDiscounts());
        create.setPrice(price);
        create.setDuration(duration);
        create.setDiscounts(discounts);
        return productMapper.productToProductDto(productRepository.save(create));
    }

    @Override
    @Transactional
    public Optional<ProductDto> updatePriceOfProduct(ProductDto productDto) {
        Long productId = productDto.getId();
        return productRepository
                .findById(productId)
                .map(product -> {
                    Price price = priceRepository.save(
                            priceMapper.priceDtoToPrice(productDto.getPriceDto())
                    );
                    productRepository.updatePriceForProductById(productId, price);
                    return productMapper.productToProductDto(
                            productRepository.findById(productId).get()
                    );
                });
    }

    @Override
    @Transactional
    public Optional<DiscountDto> applyDiscountToProduct(long productId, DiscountDto discountDto) {
        return productRepository.getProductByIdWithDiscounts(productId)
                .map(product -> {
                    Discount created = discountRepository.save(
                            discountMapper.discountDtoToDiscount(discountDto)
                    );
                    product.getDiscounts().add(created);
                    return discountMapper.dicountToDiscountDto(created);
                });
    }

    @Override
    @Transactional
    public Optional<DiscountDto> applyBulkDiscountToAllProducts(DiscountDto discountDto) {
        List<Product> products = productRepository.getAllProductsWithDiscounts();
        if (products.isEmpty()) {
            return Optional.empty();
        }

        Discount created = discountRepository.save(
                discountMapper.discountDtoToDiscount(discountDto)
        );
        products.forEach(product -> product.getDiscounts().add(created));

        return Optional.of(discountMapper.dicountToDiscountDto(created));
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
