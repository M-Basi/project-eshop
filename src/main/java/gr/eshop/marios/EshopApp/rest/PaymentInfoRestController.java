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

@RestController
@RequestMapping("/api/paymentInfo")
@RequiredArgsConstructor
public class PaymentInfoRestController {

    private final CustomerServiceImpl customerService;
    //    private final OrderServiceImpl orderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInfoRestController.class);


    @Operation(
            summary = "Save payment information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment information saved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentInfoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/save")
    public ResponseEntity<PaymentInfoReadOnlyDTO> savePaymentInfo(
            @Valid @RequestBody PaymentInfoInsertDTO paymentInfoInsertDTO,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {
        LOGGER.info("Try to Save PaymentInfo: {}", paymentInfoInsertDTO);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        PaymentInfoReadOnlyDTO paymentInfoReadOnlyDTO = customerService.savePaymentInfo(paymentInfoInsertDTO);
        return new ResponseEntity<>(paymentInfoReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update payment information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment information updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentInfoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/update")
    public ResponseEntity<PaymentInfoReadOnlyDTO> updatePaymentInfo(
            @Valid @RequestBody PaymentInfoUpdateDTO paymentInfoUpdateDTO,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException,
            AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {
        LOGGER.info("Try to update PaymentInfo: {}", paymentInfoUpdateDTO);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        PaymentInfoReadOnlyDTO paymentInfoReadOnlyDTO = customerService.updatePaymentInfo(paymentInfoUpdateDTO);
        return new ResponseEntity<>(paymentInfoReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get payment information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment information retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentInfoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/get")
    public ResponseEntity<PaymentInfoReadOnlyDTO> getPaymentInfo(
            @Valid @RequestBody IdRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        PaymentInfoReadOnlyDTO paymentInfoReadOnlyDTO = customerService.getPaymentInfoById(dto.getId());
        return new ResponseEntity<>(paymentInfoReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get payment information by customer UUID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment information retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentInfoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/getByCustomer")
    public ResponseEntity<PaymentInfoReadOnlyDTO> getPaymentInfoByCustomer(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        PaymentInfoReadOnlyDTO paymentInfoReadOnlyDTO = customerService.getPaymentInfoByCustomerUuid(dto.getUuid());
        return new ResponseEntity<>(paymentInfoReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete payment information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment information deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentInfoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/delete")
    public ResponseEntity<PaymentInfoReadOnlyDTO> delete(
            @Valid @RequestBody IdRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        PaymentInfoReadOnlyDTO paymentInfoReadOnlyDTO = customerService.deletePaymentInfo(dto.getId());
        return new ResponseEntity<>(paymentInfoReadOnlyDTO, HttpStatus.OK);
    }

}
