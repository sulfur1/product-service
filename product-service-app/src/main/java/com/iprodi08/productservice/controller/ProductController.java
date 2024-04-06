package com.iprodi08.productservice.controller;

import com.iprodi08.productservice.dto.DiscountDto;
import com.iprodi08.productservice.dto.ProductDto;
import com.iprodi08.productservice.error.NotFoundException;
import com.iprodi08.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new product", description = "Returns a dto of product created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create successfully")
    })
    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Operation(
            summary = "Update active of product",
            description = "Update active of product by productId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Update successfully")
    })
    @PatchMapping(value = "/products/{productId}/change_active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeActiveProduct(
            @PathVariable @Min(1L) long productId,
            @RequestParam("active") boolean active
    ) {

        if (active) {
            productService.acivateProduct(productId);
            return;
        }
        productService.diactivateProduct(productId);

    }

    @Operation(
            summary = "Update price of product",
            description = "Returns a dto of product with price updated"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successfully")
    })
    @PutMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updatePriceOfProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.updatePriceOfProduct(productDto).get();
    }

    @Operation(
            summary = "Apply discount to product",
            description = "Apply and return discount to product by productId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Apply successfully")
    })
    @PutMapping(value = "/products/{productId}/id/apply_discount")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void applyDiscountToProduct(
            @PathVariable @Min(1L) long productId,
            @RequestBody @Valid DiscountDto discountDto
    ) {
        productService.applyDiscountToProduct(productId, discountDto);
    }

    @Operation(
            summary = "Apply discount to all product",
            description = "Apply and return discount to all product"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Apply successfully")
    })
    @PutMapping(value = "/products/all/apply_discount", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void applyBulkDiscountForAllProducts(@Valid @RequestBody DiscountDto discountDto) {
        productService.applyBulkDiscountToAllProducts(discountDto);
    }

    @Operation(
            summary = "Change active of discount for all product",
            description = "Change active of discount by discountId for all product"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Change active successfully")
    })
    @PatchMapping(value = "/products/all/discounts/{discountId}/id/change_active")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changeActiveDiscountForAllProducts(
            @PathVariable @Min(1L) long discountId,
            @RequestParam("active") boolean active
    ) {

        if (active) {
            productService.activateDiscountForAllProducts(discountId);
            return;
        }
        productService.diactivateDiscountForAllProducts(discountId);

    }

    @Operation(
            summary = "Change active of discount for product",
            description = "Change active of discount by discountId for product by productId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Change active successfully")
    })
    @PatchMapping(value = "/products/{productId}/id/discounts/{discountId}/id/change_active")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changeActiveDiscountForProduct(
            @PathVariable @Min(1L) long productId,
            @PathVariable @Min(1L) long discountId,
            @RequestParam("active") boolean active
    ) {

        if (active) {
            productService.activateDiscountForProduct(discountId, productId);
            return;
        }
        productService.diactivateDiscountForProduct(discountId, productId);
    }

    @Operation(
            summary = "Get all products",
            description = "Get all products with price, duration and discounts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping(value = "/products/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts(@PageableDefault(size = 25) Pageable pageable) {

        return productService.getAllProducts(pageable);
    }

    @Operation(
            summary = "Get all products with sort discount",
            description = "Get all products with price, duration and discounts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping(value = "/products/all/sort-discount")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProductsBySortDiscount(
            @SortDefault(sort = "discounts.value")
            @PageableDefault(size = 25) Pageable pageable
    ) {

        return productService.getAllProducts(pageable);
    }

    @Operation(
            summary = "Get all products with sort price",
            description = "Get all products with price, duration and discounts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping(value = "/products/all/sort-price")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProductsBySortPrice(
            @SortDefault(sort = "price.value")
            @PageableDefault(size = 25) Pageable pageable
    ) {

        return productService.getAllProducts(pageable);
    }

    @Operation(
            summary = "Get product by name",
            description = "Get product with price, duration and discounts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping(value = "/products/all/by-name")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProductsWithName(
            @PageableDefault(size = 25) Pageable pageable,
            @RequestParam("name") String name) {

        return productService.getAllProductsByName(pageable, name);
    }

    @Operation(
            summary = "Get all products by active",
            description = "Get all products with price, duration and discounts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping(value = "/products/all/by-active")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProductsWithActive(
            @PageableDefault(size = 25) Pageable pageable,
            @RequestParam("active") boolean active) {

        return productService.getAllProductsByActive(pageable, active);
    }

    @Operation(
            summary = "Get product by productId",
            description = "Get product with price, duration and discounts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received")
    })
    @GetMapping(value = "/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable @Min(1L) long productId) {

        return productService
                        .getProductById(productId)
                        .orElseThrow(() -> new NotFoundException(
                                String.format("Product with id: %s not found", productId))
                        );
    }

}
