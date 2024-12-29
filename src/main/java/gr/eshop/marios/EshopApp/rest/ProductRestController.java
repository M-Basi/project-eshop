package gr.eshop.marios.EshopApp.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.dto.ProductInsertDTO;
import gr.eshop.marios.EshopApp.dto.ProductReadOnlyDTO;
import gr.eshop.marios.EshopApp.dto.ProductUpdateDTO;
import gr.eshop.marios.EshopApp.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductRestController { //todo

    private final ProductServiceImpl productService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);


    @Operation(
            summary = "Save a new product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product saved successfully",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error saving product",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> saveProduct(
            @RequestPart(name = "product") String productJson,
            @RequestPart("photoProduct") MultipartFile photoProduct) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductInsertDTO productInsertDTO;
        try {
            productInsertDTO = objectMapper.readValue(productJson, ProductInsertDTO.class);
            productService.saveProduct(productInsertDTO, photoProduct);
        } catch (Exception e) {
            LOGGER.error("Error processing product:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error saving product"));
        }

        return ResponseEntity.ok(Map.of("message", "Product saved successfully!"));
    }

    @Operation(
            summary = "Update an existing product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error updating product",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> updateProduct(
            @RequestPart(name = "product") String productJson,
            @RequestPart("photoProduct") MultipartFile photoProduct) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductUpdateDTO productUpdateDTO;
        try {
            productUpdateDTO = objectMapper.readValue(productJson, ProductUpdateDTO.class);
            productService.updateProduct(productUpdateDTO, photoProduct);
        } catch (Exception e) {
            LOGGER.error("Error processing product:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error updating product"));
        }

        return ResponseEntity.ok(Map.of("message", "Product updated successfully!"));
    }

    @Operation(
            summary = "Delete a product by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/delete")
    public ResponseEntity<ProductReadOnlyDTO> deleteProduct(
            @Valid @RequestBody Long productId,
            BindingResult bindingResult) throws AppObjectNotFoundException, ValidationException, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        ProductReadOnlyDTO productReadOnlyDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productReadOnlyDTO, HttpStatus.OK);
    }

}
