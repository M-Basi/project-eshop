package gr.eshop.marios.EshopApp.rest;

import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.filters.ProductFilters;
import gr.eshop.marios.EshopApp.dto.ProductReadOnlyDTO;
import gr.eshop.marios.EshopApp.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsAllRestController {

    private final ProductServiceImpl productService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsAllRestController.class);

    @Operation(
            summary = "Get paginated list of products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Products retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("/pageable")
    public ResponseEntity<Page<ProductReadOnlyDTO>> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductReadOnlyDTO> productsPage = productService.getPaginatedProducts(page, size);
        return new ResponseEntity<>(productsPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Get products with filters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filtered products retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Paginated.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/all")
    public ResponseEntity<Paginated<ProductReadOnlyDTO>> getProducts(
            @Nullable @RequestBody ProductFilters filters)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        LOGGER.info("getProducts called");
        LOGGER.info("Filters: {}", filters);

        try {
            if (filters == null) filters = ProductFilters.builder().build();
            LOGGER.info("Filters: {}", filters);
            LOGGER.debug("getProducts called");
            LOGGER.info("Response: {}", productService.getProductFilteredPaginated(filters).getData());
            return ResponseEntity.ok(productService.getProductFilteredPaginated(filters));

        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get products.", e);
            throw e;
        }
    }

    @Operation(
            summary = "Get a product by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductReadOnlyDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/product")
    public ResponseEntity<ProductReadOnlyDTO> getProductById(
            @Valid @RequestBody Long productId,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        ProductReadOnlyDTO productReadOnlyDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a list of all products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Products retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("/list")
    public ResponseEntity<List<ProductReadOnlyDTO>> getAllListProducts() throws AppObjectNotFoundException {

        LOGGER.info("getAllListProducts called");
        List<ProductReadOnlyDTO> productReadOnlyDTOs = productService.getAllProductsList();
        return new ResponseEntity<>(productReadOnlyDTOs, HttpStatus.OK);

    }

    //    @PostMapping("/allPaginated")
//    public ResponseEntity<Paginated<ProductReadOnlyDTO>> getProductsPaginatedFiltered(
//            @Nullable @RequestBody ProductFilters filters)
//            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
//        try {
//            if (filters == null) filters = ProductFilters.builder().build();
//            return ResponseEntity.ok(productService.getProductFilteredPaginated(filters));
//        } catch (Exception e) {
//            LOGGER.error("ERROR: Could not get products.", e);
//            throw e;
//        }
//    }
}
