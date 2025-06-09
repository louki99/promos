package ma.foodplus.ordering.system.product.configuration;

public final class CacheConstants {
    public static final String PRODUCTS_CACHE = "products";
    public static final String PRODUCT_CACHE = "product";
    
    public static final String KEY_ALL_PRODUCTS = "'all'";
    public static final String KEY_DELIVERABLE_PRODUCTS = "'deliverable'";
    public static final String KEY_ACTIVE_PRODUCTS = "'active'";
    public static final String KEY_FAMILY_PRODUCTS = "'family:' + #familyCode";
    public static final String KEY_PRODUCT_BY_ID = "#productId";
    public static final String KEY_PRODUCT_BY_REF = "'ref:' + #reference";
    public static final String KEY_PRODUCT_BY_BARCODE = "'barcode:' + #barcode";
    public static final String KEY_EXISTS_BY_REF = "'exists:ref:' + #reference";
    public static final String KEY_EXISTS_BY_BARCODE = "'exists:barcode:' + #barcode";
    
    private CacheConstants() {
        // Prevent instantiation
    }
} 