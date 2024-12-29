package gr.eshop.marios.EshopApp.rest;


import gr.eshop.marios.EshopApp.authentication.AuthenticationService;
import gr.eshop.marios.EshopApp.core.exceptions.AppObjectNotAuthorizedException;
import gr.eshop.marios.EshopApp.dto.AuthenticationRequestDTO;
import gr.eshop.marios.EshopApp.dto.AuthenticationResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);
    private final AuthenticationService authenticationService;


    @Operation(
            summary = "Authenticate a user and retrieve an authentication token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid authentication request",
                            content = @Content
                    )
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO)
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        LOGGER.info("User authenticated");
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }

//    @PostMapping("/refreshToken")
//    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestBody AuthenticationRequestDTO authenticationRequestDTO)
//            throws AppObjectNotAuthorizedException {
//        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
//        LOGGER.info("User authenticated");
//        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
//    }

}
