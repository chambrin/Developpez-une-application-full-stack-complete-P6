package com.openclassrooms.mddapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlerManager {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandlerManager.class);
    private static final String GENERIC_ERROR_MESSAGE = "Une erreur s'est produite, veuillez réessayer.";
    private static final String VALIDATION_LOG_PATTERN = "Erreur de validation sur le champ '{}': {}";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manageGenericError(Exception exception, WebRequest request) {
        log.error("Une erreur s'est produite: ", exception);
        return buildErrorResponse(GENERIC_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> manageIllegalArgument(IllegalArgumentException exception, WebRequest request) {
        log.warn("Argument illégal: {}", exception.getMessage());
        return buildObjectResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> manageUserNotFound(UsernameNotFoundException exception, WebRequest request) {
        log.warn("Nom d'utilisateur non trouvé: {}", exception.getMessage());
        return buildObjectResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> manageRuntimeError(RuntimeException exception, WebRequest request) {
        log.error("Erreur d'exécution: {}", exception.getMessage());
        return buildObjectResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> manageValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrors = extractValidationErrors(exception);
        return buildObjectResponse(validationErrors, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(message);
    }

    private ResponseEntity<Object> buildObjectResponse(Object body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    private Map<String, String> extractValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            log.warn(VALIDATION_LOG_PATTERN, fieldError.getField(), fieldError.getDefaultMessage());
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return errorMap;
    }
}