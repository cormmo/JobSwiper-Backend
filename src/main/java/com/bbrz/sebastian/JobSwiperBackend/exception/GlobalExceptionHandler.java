package com.bbrz.sebastian.JobSwiperBackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles application-wide exceptions and converts them into API error responses.
 *
 * <p>Also handles authentication and access denied errors from Spring Security.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final Logger LOGGER =
            Logger.getLogger(GlobalExceptionHandler.class.getName());

    private final ObjectMapper objectMapper;

    /**
     * Creates the global exception handler.
     *
     * @param objectMapper mapper used for writing JSON responses
     */
    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles missing resources.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 404 error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError> notFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles conflicts with existing data.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 409 error response
     */
    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    ResponseEntity<ApiError> conflict(Exception ex, HttpServletRequest request) {
        String message = ex instanceof ConflictException
                ? ex.getMessage()
                : "The request conflicts with existing data";

        return error(HttpStatus.CONFLICT, message, request);
    }

    /**
     * Handles forbidden operations.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 403 error response
     */
    @ExceptionHandler(ForbiddenOperationException.class)
    ResponseEntity<ApiError> forbidden(ForbiddenOperationException ex, HttpServletRequest request) {
        return error(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    /**
     * Handles invalid login credentials.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 401 error response
     */
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ApiError> badCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, "Invalid username or password", request);
    }

    /**
     * Handles validation errors in request bodies.
     *
     * @param ex validation exception
     * @param request current request
     * @return validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(field ->
                        fields.putIfAbsent(field.getField(), field.getDefaultMessage()));

        ex.getBindingResult().getGlobalErrors()
                .forEach(error ->
                        fields.putIfAbsent(error.getObjectName(), error.getDefaultMessage()));

        return validationError(request, fields);
    }

    /**
     * Handles constraint validation errors.
     *
     * @param ex validation exception
     * @param request current request
     * @return validation error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> constraintValidation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, String> fields = new LinkedHashMap<>();

        ex.getConstraintViolations()
                .forEach(v ->
                        fields.put(v.getPropertyPath().toString(), v.getMessage()));

        return validationError(request, fields);
    }

    /**
     * Handles invalid request values.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 400 error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(IllegalArgumentException ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles invalid or missing request bodies.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 400 error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> unreadableBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return error(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or contains an invalid value",
                request
        );
    }

    /**
     * Handles missing or invalid request parameters.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 400 error response
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    ResponseEntity<ApiError> invalidRequestParameter(
            Exception ex,
            HttpServletRequest request) {

        return error(
                HttpStatus.BAD_REQUEST,
                "A request parameter is missing or contains an invalid value",
                request
        );
    }

    /**
     * Handles requests for unknown resources.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 404 error response
     */
    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ApiError> noResource(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        return error(HttpStatus.NOT_FOUND, "Resource not found", request);
    }

    /**
     * Handles unsupported HTTP methods.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 405 error response
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiError> methodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        return error(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Request method is not supported",
                request
        );
    }

    /**
     * Handles unsupported content types.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 415 error response
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ApiError> unsupportedMediaType(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {

        return error(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Content type is not supported",
                request
        );
    }

    /**
     * Handles access denied errors.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 403 error response
     */
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiError> accessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return error(HttpStatus.FORBIDDEN, "Access is denied", request);
    }

    /**
     * Handles authentication errors.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 401 error response
     */
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiError> authentication(
            AuthenticationException ex,
            HttpServletRequest request) {

        return error(HttpStatus.UNAUTHORIZED, "Authentication is required", request);
    }

    /**
     * Handles unexpected application errors.
     *
     * @param ex thrown exception
     * @param request current request
     * @return 500 error response
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest request) {
        LOGGER.log(Level.SEVERE,
                "Unhandled exception for " + request.getRequestURI(), ex);

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }

    /**
     * Handles unauthenticated requests from Spring Security.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {

        writeSecurityError(
                response,
                HttpStatus.UNAUTHORIZED,
                "Authentication is required",
                request
        );
    }

    /**
     * Handles forbidden requests from Spring Security.
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception) throws IOException {

        writeSecurityError(
                response,
                HttpStatus.FORBIDDEN,
                "Access is denied",
                request
        );
    }

    /**
     * Creates a validation error response.
     */
    private ResponseEntity<ApiError> validationError(
            HttpServletRequest request,
            Map<String, String> fields) {

        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fields
        );

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }

    /**
     * Creates a standard API error response.
     */
    private ResponseEntity<ApiError> error(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError(status, message, request));
    }

    /**
     * Writes a security error directly to the HTTP response.
     */
    private void writeSecurityError(
            HttpServletResponse response,
            HttpStatus status,
            String message,
            HttpServletRequest request) throws IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        objectMapper.writeValue(
                response.getOutputStream(),
                apiError(status, message, request)
        );
    }

    /**
     * Creates an {@link ApiError} object.
     */
    private ApiError apiError(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                Objects.requireNonNullElse(message, status.getReasonPhrase()),
                request.getRequestURI()
        );
    }
}