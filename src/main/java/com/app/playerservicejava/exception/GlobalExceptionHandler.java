package com.app.playerservicejava.exception;

import com.app.playerservicejava.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(PlayerNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = String.format("Parameter '%s' value '%s' is invalid for required type '%s'",
                ex.getName(), ex.getValue(), Objects.nonNull(ex.getRequiredType()) ? ex.getRequiredType().getSimpleName() : "unknown");
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", msg, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> String.format("Field '%s' %s", fe.getField(), fe.getDefaultMessage()))
                .orElse("Validation failed");
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", msg, req);
    }

    // -------- Data/Repository exceptions --------
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> handleEmptyResult(EmptyResultDataAccessException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found", req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "CONSTRAINT_VIOLATION", "Duplicate or invalid data", req);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccess(DataAccessException ex, HttpServletRequest req) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", msg, req);
    }

    // -------- Fallback --------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage(), req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String error, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ApiError(error, message, status.value(), req.getRequestURI()));
    }

    @ExceptionHandler(AdminAccessException.class)
    public ResponseEntity<?> handleAdminAccess(AdminAccessException adminAccessException) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "ADMIN_API_ERROR");
        error.put("message", adminAccessException.getMessage());
        error.put("status", 403);
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(GuestAccessException.class)
    public ResponseEntity<String> handleGuestAccess(GuestAccessException guestAccessException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(guestAccessException.getMessage());
    }
}
