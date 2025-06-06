package ma.foodplus.ordering.system.product.service.domain.entity;


import ma.foodplus.ordering.system.domain.entity.AggregateRoot;
import ma.foodplus.ordering.system.domain.valueobject.StoreId;

import java.util.List;

public class Store extends AggregateRoot<StoreId>{
    private final List<Product> products;
    private boolean active;

    private Store(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public static final class Builder {
        private StoreId restaurantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(StoreId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Store build() {
            return new Store(this);
        }
    }
}
