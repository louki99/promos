package ma.foodplus.ordering.system.product.dto.category;

import java.time.ZonedDateTime;
import java.util.List;

public record CategoryDTO(
    Long id,
    String code,
    String name,
    String description,
    Integer level,
    Long parentId,
    List<CategoryDTO> children,
    boolean isActive,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {} 