package gr.eshop.marios.EshopApp.rest;

import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.core.filters.CustomerFilters;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.dto.*;
import gr.eshop.marios.EshopApp.repository.OrderRepository;
import gr.eshop.marios.EshopApp.service.CustomerServiceImpl;
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

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerServiceImpl customerService;
//    private final OrderServiceImpl orderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRestController.class);
    private final OrderRepository orderRepository;


    @Operation(
            summary = "Get paginated customers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved paginated customers",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    )
            }
    )
    @PostMapping("/all")
    public ResponseEntity<Page<CustomerReadOnlyDTO>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CustomerReadOnlyDTO> customerPage = customerService.getPaginatedCustomers(page, size);
        return new ResponseEntity<>(customerPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered and paginated customers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved filtered and paginated customers",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Paginated.class)
                            )
                    )
            }
    )
    @PostMapping("/allPaginated")
    public ResponseEntity<Paginated<CustomerReadOnlyDTO>> getCustomerPaginatedFiltered(
            @Nullable @RequestBody CustomerFilters filters)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = CustomerFilters.builder().build();
            return ResponseEntity.ok(customerService.getCustomersFilteredPaginated(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get products.", e);
            throw e;
        }
    }

    @Operation(
            summary = "Save a new customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer saved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customer/save")
    public ResponseEntity<CustomerReadOnlyDTO> saveCustomer(
            @Valid @RequestBody CustomerInsertDTO customerInsertDTO,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.saveCustomer(customerInsertDTO);
        LOGGER.info("Returning CustomerReadOnlyDTO: {}", customerReadOnlyDTO);

        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customer/update")
    public ResponseEntity<CustomerReadOnlyDTO> updateCustomer(
            @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.updateCustomer(customerUpdateDTO);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customer/delete")
    public ResponseEntity<CustomerReadOnlyDTO> deleteCustomer(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        String uuid = dto.getUuid();
        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.deleteCustomer(uuid);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get customer by user UUID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customer/userUuid")
    public ResponseEntity<CustomerReadOnlyDTO> getCustomerByUserUuid(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        String uuid = dto.getUuid();
        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.getCustomerByUserUuid(uuid);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);
    }


    @Operation(
            summary = "Get customer by UUID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customer/customer")
    public ResponseEntity<CustomerReadOnlyDTO> getCustomerByUuid(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        String uuid = dto.getUuid();
        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.getCustomerByUuid(uuid);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);
    }

}
