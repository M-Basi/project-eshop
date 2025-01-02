package gr.eshop.marios.EshopApp.rest;

import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.core.filters.Paginated;
import gr.eshop.marios.EshopApp.core.filters.UserFilter;
import gr.eshop.marios.EshopApp.dto.*;
import gr.eshop.marios.EshopApp.repository.UserRepository;
import gr.eshop.marios.EshopApp.service.UserServiceImpl;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {


    private final UserServiceImpl userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserRepository userRepository;

    @Operation(
            summary = "Get all users (paginated)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/all")
    public ResponseEntity<Page<UserReadOnlyDTO>> getUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserReadOnlyDTO> usersPage = userService.getPaginatedUsers(page, size);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered users (paginated)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filtered users retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Paginated.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/allPaginated")
    public ResponseEntity<Paginated<UserReadOnlyDTO>> getUsersPaginatedFiltered(
            @Nullable @RequestBody UserFilter filters)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = UserFilter.builder().build();
            return ResponseEntity.ok(userService.getUsersFilteredPaginated(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get users.", e);
            throw e;
        }
    }

    @Operation(
            summary = "Register a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserReadOnlyDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserReadOnlyDTO> saveUser(
            @Valid @RequestBody UserInsertDTO userInsertDTO,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserReadOnlyDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/user/update")
    public ResponseEntity<UserReadOnlyDTO> updateUser(
            @Valid @RequestBody UserUpdateDTO userUpdateDTO,
            BindingResult bindingResult) throws AppObjectNotFoundException, ValidationException, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.updateUser(userUpdateDTO);
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserReadOnlyDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/user/delete")
    public ResponseEntity<UserReadOnlyDTO> deleteUser(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.deleteUserByUuid(dto.getUuid());
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a user by UUID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserReadOnlyDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/user/get")
    public ResponseEntity<UserReadOnlyDTO> getUser(
            @Valid @RequestBody UuidRequestDTO dto,
            BindingResult bindingResult) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, ValidationException, AppObjectAlreadyExists, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.getUserByUuid(dto.getUuid());
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

}
