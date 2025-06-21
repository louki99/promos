package ma.foodplus.ordering.system.partner.exception;

public enum ErrorCode {
    // Partner related errors (5000-5099)
    PARTNER_NOT_FOUND(5000, "Partner not found"),
    PARTNER_DUPLICATE_CT_NUM(5001, "Partner with this CT number already exists"),
    PARTNER_DUPLICATE_ICE(5002, "Partner with this ICE already exists"),
    PARTNER_INVALID_INPUT(5003, "Invalid input for partner"),
    PARTNER_INACTIVE(5004, "Partner is inactive"),
    PARTNER_CREDIT_LIMIT_EXCEEDED(5005, "Partner credit limit exceeded"),
    PARTNER_CONTRACT_EXPIRED(5006, "Partner contract has expired"),
    PARTNER_CONTRACT_NOT_FOUND(5007, "Partner contract not found"),
    PARTNER_GROUP_NOT_FOUND(5008, "Partner group not found"),
    PARTNER_ALREADY_IN_GROUP(5009, "Partner is already in this group"),
    PARTNER_NOT_IN_GROUP(5010, "Partner is not in this group"),
    
    // Validation errors (5100-5199)
    VALIDATION_CT_NUM_FORMAT(5100, "Invalid CT number format"),
    VALIDATION_ICE_FORMAT(5101, "Invalid ICE format"),
    VALIDATION_EMAIL_FORMAT(5102, "Invalid email format"),
    VALIDATION_PHONE_FORMAT(5103, "Invalid phone number format"),
    VALIDATION_CREDIT_LIMIT_NEGATIVE(5104, "Credit limit cannot be negative"),
    VALIDATION_CONTRACT_DATES(5105, "Contract end date must be after start date"),
    VALIDATION_LOYALTY_POINTS_NEGATIVE(5106, "Loyalty points cannot be negative"),
    
    // Business logic errors (5200-5299)
    BUSINESS_CONTRACT_EXPIRED(5200, "Contract has expired"),
    BUSINESS_CREDIT_LIMIT_EXCEEDED(5201, "Credit limit exceeded for this operation"),
    BUSINESS_PARTNER_INACTIVE(5202, "Partner is inactive for this operation"),
    BUSINESS_VIP_STATUS_REQUIRED(5203, "VIP status required for this operation"),
    BUSINESS_MINIMUM_ORDER_NOT_MET(5204, "Minimum order requirement not met"),
    BUSINESS_PAYMENT_OVERDUE(5205, "Payment is overdue"),
    
    // System errors (9000-9999)
    SYSTEM_ERROR(9000, "System error"),
    DATABASE_ERROR(9001, "Database error"),
    CACHE_ERROR(9002, "Cache error"),
    EXTERNAL_SERVICE_ERROR(9003, "External service error"),
    NETWORK_ERROR(9004, "Network error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 