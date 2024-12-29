package gr.eshop.marios.EshopApp.rest;

import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.dto.*;
import gr.eshop.marios.EshopApp.service.CustomerServiceImpl;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/customerInfo")
@RequiredArgsConstructor
public class CustomerInfoRestController {

    private final CustomerServiceImpl customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInfoRestController.class);

    @Operation(
            summary = "Save Customer Info",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Info saved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerInfoReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/save")
    public ResponseEntity<CustomerInfoReadOnlyDTO> saveCustomerInfo(
            @Valid @RequestBody CustomerInfoInsertDTO customerInfoInsertDTO,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {
        LOGGER.info("Try to Save CustomerInfo: {}", customerInfoInsertDTO);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);

        }

        CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO = customerService.saveCustomerInfo(customerInfoInsertDTO);
        LOGGER.info("Returning CustomerInfoReadOnlyDTO: {}", customerInfoReadOnlyDTO);

        return new ResponseEntity<>(customerInfoReadOnlyDTO, HttpStatus.OK);

    }

    @Operation(
            summary = "Update Customer Info",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Info updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerInfoReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/update")
    public ResponseEntity<CustomerInfoReadOnlyDTO> updateCustomerInfo(
            @Valid @RequestBody CustomerInfoUpdateDTO customerInfoUpdateDTO,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {
        LOGGER.info("Try to update CustomerInfo: {}", customerInfoUpdateDTO);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO = customerService.updateCustomerInfo(customerInfoUpdateDTO);
//        LOGGER.info("Returning CustomerInfoReadOnlyDTO: {}", customerInfoReadOnlyDTO);

        return new ResponseEntity<>(customerInfoReadOnlyDTO, HttpStatus.OK);
    }



    @Operation(
            summary = "Get Customer Info by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Info retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerInfoReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/get")
    public ResponseEntity<CustomerInfoReadOnlyDTO> getCustomerInfoById(
            @Valid @RequestBody IdRequestDTO dto, BindingResult bindingResult)
            throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Long id = dto.getId();

        CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO = customerService.getCustomerInfoById(id);
        return new ResponseEntity<>(customerInfoReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get Customer Info by Customer UUID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Info retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerInfoReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/getByCustomer")
    public ResponseEntity<CustomerInfoReadOnlyDTO> getCustomerInfoByCustomerUuid(
            @RequestBody UuidRequestDTO dto, BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {
        if (bindingResult.hasErrors()) {
            LOGGER.error("Validation errors: {}", bindingResult.getAllErrors());
            throw new ValidationException(bindingResult);
        }

        String uuid = dto.getUuid();
        LOGGER.info("Try to Get CustomerInfo for customer with uuid: {}", uuid);


        CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO = customerService.getCustomerInfoByCustomerUuid(uuid);
        return new ResponseEntity<>(customerInfoReadOnlyDTO, HttpStatus.OK);
    }


    @Operation(
            summary = "Delete Customer Info",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Info deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerInfoReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/delete")
    public ResponseEntity<CustomerInfoReadOnlyDTO> delete(
            @Valid @RequestBody IdRequestDTO dto, BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, AppObjectNotFoundException,
            ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        Long id = dto.getId();
        LOGGER.info("Try to delete CustomerInfo with id: {}", id);

        CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO = customerService.deleteCustomerInfo(id);
        return new ResponseEntity<>(customerInfoReadOnlyDTO, HttpStatus.OK);
    }
}
