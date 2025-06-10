-- Add wholesale pricing fields
ALTER TABLE products
    ADD COLUMN wholesale_minimum_quantity INTEGER NOT NULL DEFAULT 1,
    ADD COLUMN wholesale_discount_percentage DECIMAL(5,2),
    ADD COLUMN wholesale_tier_1_price DECIMAL(10,2),
    ADD COLUMN wholesale_tier_1_quantity INTEGER,
    ADD COLUMN wholesale_tier_2_price DECIMAL(10,2),
    ADD COLUMN wholesale_tier_2_quantity INTEGER,
    ADD COLUMN wholesale_tier_3_price DECIMAL(10,2),
    ADD COLUMN wholesale_tier_3_quantity INTEGER;

-- Add bulk order fields
ALTER TABLE products
    ADD COLUMN maximum_order_quantity INTEGER,
    ADD COLUMN bulk_discount_threshold INTEGER,
    ADD COLUMN bulk_discount_percentage DECIMAL(5,2),
    ADD COLUMN bulk_package_size INTEGER,
    ADD COLUMN bulk_package_unit VARCHAR(20);

-- Add operational flags
ALTER TABLE products
    ADD COLUMN is_wholesale_only BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN requires_contract BOOLEAN NOT NULL DEFAULT false;

-- Create indexes for frequently queried fields
CREATE INDEX idx_products_wholesale ON products(is_wholesale_only, wholesale_minimum_quantity);
CREATE INDEX idx_products_bulk ON products(is_bulk_item, bulk_discount_threshold);
CREATE INDEX idx_products_contract ON products(requires_contract); 