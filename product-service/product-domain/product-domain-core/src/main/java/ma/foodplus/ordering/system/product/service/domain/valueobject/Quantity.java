package ma.foodplus.ordering.system.product.service.domain.valueobject;

import ma.foodplus.ordering.system.product.service.domain.exception.NegativeQuantityException;

public record Quantity(int amount) {
    public Quantity {
        if (amount < 0) throw new NegativeQuantityException("Quantity cannot be negative");
    }

    public boolean isAvailable() {
        return amount > 0;
    }
}

