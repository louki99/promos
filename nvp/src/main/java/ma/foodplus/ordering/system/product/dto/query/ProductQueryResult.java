package ma.foodplus.ordering.system.product.dto.query;

import ma.foodplus.ordering.system.product.dto.response.ProductResponse;

import java.util.List;

public record ProductQueryResult(
    List<ProductResponse> products,
    long totalElements,
    int totalPages,
    int currentPage,
    int pageSize
) {} 