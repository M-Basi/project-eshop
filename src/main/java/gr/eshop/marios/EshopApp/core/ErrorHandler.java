package gr.eshop.marios.EshopApp.core;


import gr.eshop.marios.EshopApp.core.exceptions.*;
import gr.eshop.marios.EshopApp.dto.ResponseMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles validation exceptions by returning a map of field errors.
     *
     * @param ex the ValidationException containing the binding result with errors
     * @return a ResponseEntity containing the map of field errors and a BAD_REQUEST status
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String , String>> handleValidationException(ValidationException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        Map<String , String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles exceptions when an object is not found.
     *
     * @param e the AppObjectNotFoundException containing the error details
     * @return a ResponseEntity containing a ResponseMessageDTO and a NOT_FOUND status
     */
    @ExceptionHandler({AppObjectNotFoundException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectNotFoundException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions when an object already exists.
     *
     * @param e the AppObjectAlreadyExists exception containing the error details
     * @return a ResponseEntity containing a ResponseMessageDTO and a CONFLICT status
     */
    @ExceptionHandler({AppObjectAlreadyExists.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectAlreadyExists e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions for invalid arguments in application objects.
     *
     * @param e the AppObjectInvalidArgumentException containing the error details
     * @return a ResponseEntity containing a ResponseMessageDTO and a BAD_REQUEST status
     */
    @ExceptionHandler({AppObjectInvalidArgumentException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectInvalidArgumentException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when an object is not authorized for access.
     *
     * @param e the AppObjectNotAuthorizedException containing the error details
     * @return a ResponseEntity containing a ResponseMessageDTO and a FORBIDDEN status
     */
    @ExceptionHandler({AppObjectNotAuthorizedException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectNotAuthorizedException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles server exceptions occurring in the application.
     *
     * @param e the AppServerException containing the error details
     * @return a ResponseEntity containing a ResponseMessageDTO and an INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler({AppServerException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppServerException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}