package gr.eshop.marios.EshopApp.rest;


import gr.eshop.marios.EshopApp.dto.BrandReadOnlyDTO;
import gr.eshop.marios.EshopApp.dto.CategoryReadOnlyDTO;
import gr.eshop.marios.EshopApp.model.static_data.Region;
import gr.eshop.marios.EshopApp.service.BrandServiceImpl;
import gr.eshop.marios.EshopApp.service.CategoryServiceImpl;
import gr.eshop.marios.EshopApp.service.RegionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CatBrandRestController {

    private final CategoryServiceImpl categoryService;
    private final BrandServiceImpl brandService;
    private final RegionServiceImpl regionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CatBrandRestController.class);

    @Operation(
            summary = "Retrieve all brands",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of brands retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BrandReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    @GetMapping("/brands")
    public ResponseEntity<List<BrandReadOnlyDTO>> getBrands (){

        List<BrandReadOnlyDTO> brands= brandService.getAllBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }


    @Operation(
            summary = "Retrieve all categories",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of categories retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryReadOnlyDTO>> getCategories (){

        List<CategoryReadOnlyDTO> categories= categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }


    @Operation(
            summary = "Retrieve all regions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of regions retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Region.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getRegions (){
        LOGGER.info("getRegions");
        List<Region> regions = regionService.getAllRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }



}
