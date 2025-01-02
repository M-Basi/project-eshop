package gr.eshop.marios.EshopApp.rest;

import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.dto.*;

import gr.eshop.marios.EshopApp.repository.OrderRepository;
import gr.eshop.marios.EshopApp.service.CustomerServiceImpl;
import gr.eshop.marios.EshopApp.service.OrderServiceImpl;
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


import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final CustomerServiceImpl customerService;
    private final OrderServiceImpl orderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);
    private final OrderRepository orderRepository;

    @Operation(
            summary = "Save an order",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order saved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/save")
    public ResponseEntity<OrderReadOnlyDTO> saveOrder(
            @Valid @RequestBody OrderInsertDTO orderInsertDTO,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {
        LOGGER.info("Saving order: {}", orderInsertDTO.getStatus());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        OrderReadOnlyDTO orderReadOnlyDTO = orderService.saveOrderToCustomer(orderInsertDTO);
        LOGGER.info("Returning OrderReadOnlyDTO: {}", orderReadOnlyDTO);

        return new ResponseEntity<>(orderReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all orders for a customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Orders retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Set.class)
                            )
                    )
            }
    )
    @PostMapping("/all")
    public ResponseEntity<List<OrderReadOnlyDTO>> getOrders(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException,
            AppObjectAlreadyExists, AppServerException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        String uuid = dto.getUuid();

        List<OrderReadOnlyDTO> orderReadOnlyDTO = orderService.getAllOrders(uuid);

        return new ResponseEntity<>(orderReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a specific order by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/order")
    public ResponseEntity<OrderReadOnlyDTO> getOrder(
            @Valid @RequestBody IdRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException,
            AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        OrderReadOnlyDTO order = orderService.getOrderById(dto.getId());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}