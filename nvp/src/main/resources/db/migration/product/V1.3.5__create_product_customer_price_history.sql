CREATE TABLE IF NOT EXISTS product_customer_price_history (
    id BIGSERIAL PRIMARY KEY,
    product_customer_id BIGINT NOT NULL,
    old_price DECIMAL(24,6),
    new_price DECIMAL(24,6),
    old_discount DECIMAL(24,6),
    new_discount DECIMAL(24,6),
    change_date TIMESTAMP WITH TIME ZONE NOT NULL,
    change_reason VARCHAR(255),
    changed_by VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_customer_id) REFERENCES products_customer(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_product_customer_price_history_product_customer_id 
ON product_customer_price_history(product_customer_id);

CREATE INDEX IF NOT EXISTS idx_product_customer_price_history_change_date 
ON product_customer_price_history(change_date);

-- Add trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_product_customer_price_history_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_product_customer_price_history_updated_at
    BEFORE UPDATE ON product_customer_price_history
    FOR EACH ROW
    EXECUTE FUNCTION update_product_customer_price_history_updated_at(); 