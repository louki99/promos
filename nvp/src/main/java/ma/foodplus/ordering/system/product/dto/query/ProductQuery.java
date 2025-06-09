package ma.foodplus.ordering.system.product.dto.query;


import ma.foodplus.ordering.system.product.enums.SuiviStock;

public record ProductQuery(
    String reference,
    String title,
    String barcode,
    String familyCode,
    String category1,
    String category2,
    String category3,
    String category4,
    Boolean deliverable,
    Boolean inactive,
    SuiviStock stockTracking,
    Integer page,
    Integer size,
    String sortBy,
    String sortDirection
) {
    public ProductQuery {
        // Default values for pagination and sorting
        page = page != null ? page : 0;
        size = size != null ? size : 10;
        sortBy = sortBy != null ? sortBy : "reference";
        sortDirection = sortDirection != null ? sortDirection : "ASC";
    }
} 