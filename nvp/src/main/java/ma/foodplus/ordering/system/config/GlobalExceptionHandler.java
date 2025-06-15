package ma.foodplus.ordering.system.config;

import ma.foodplus.ordering.system.common.exception.BaseException;
import ma.foodplus.ordering.system.common.exception.ErrorCode;
import ma.foodplus.ordering.system.order.exception.OrderItemNotFoundException;
import ma.foodplus.ordering.system.order.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFoundException(OrderNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(BaseException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.put("errorCode", ErrorCode.SYSTEM_ERROR.getCode());
        body.put("message", "An unexpected error occurred: " + ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, BaseException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("errorCode", ex.getErrorCode().getCode());
        body.put("message", ex.getMessage());
        if (ex.getDetails() != null) {
            body.put("details", ex.getDetails());
        }
        
        return new ResponseEntity<>(body, status);
    }

    private HttpStatus determineHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case ORDER_NOT_FOUND, ORDER_ITEM_NOT_FOUND, PRODUCT_NOT_FOUND, CUSTOMER_NOT_FOUND -> 
                HttpStatus.NOT_FOUND;
            case VALIDATION_ERROR, ORDER_INVALID_STATUS, ORDER_MINIMUM_VALUE_NOT_MET, 
                 ORDER_DELIVERY_ADDRESS_REQUIRED, ORDER_CREDIT_LIMIT_EXCEEDED, 
                 ORDER_INVALID_PROMO_CODE, ORDER_INVALID_DELIVERY_TIME, 
                 ORDER_INVALID_BULK_ORDER, ORDER_INVALID_LOYALTY_POINTS,
                 PRODUCT_INVALID_PRICE, PRODUCT_INVALID_QUANTITY, 
                 PRODUCT_INVALID_CATEGORY, CUSTOMER_INVALID_CREDENTIALS,
                 CUSTOMER_INVALID_ADDRESS, CUSTOMER_INVALID_PHONE,
                 CUSTOMER_INVALID_EMAIL -> 
                HttpStatus.BAD_REQUEST;
            case SYSTEM_ERROR, DATABASE_ERROR, CACHE_ERROR, EXTERNAL_SERVICE_ERROR -> 
                HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
} 