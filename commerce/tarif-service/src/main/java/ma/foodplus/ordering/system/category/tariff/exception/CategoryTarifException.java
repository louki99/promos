package ma.foodplus.ordering.system.category.tariff.exception;

public class CategoryTarifException extends BaseException {
    public CategoryTarifException(ErrorCode errorCode) {
        super(errorCode);
    }
    public CategoryTarifException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }
    public CategoryTarifException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 