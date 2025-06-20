package ma.foodplus.ordering.system.category.tariff.service;

import ma.foodplus.ordering.system.category.tariff.domain.CategoryTarif;
import org.springframework.data.jpa.domain.Specification;

public class CategoryTarifSpecifications {

    public static Specification<CategoryTarif> containsTextInNameDescOrCode(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("code")), pattern)
            );
        };
    }
}