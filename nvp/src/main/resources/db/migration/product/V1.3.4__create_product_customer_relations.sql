-- Create product-customer relations table
CREATE TABLE products_customer (
    id BIGSERIAL PRIMARY KEY,
    reference_product BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    reference_customer BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    category VARCHAR(255),
    coef DECIMAL(24,6),
    prix_ttc DECIMAL(24,6),
    qte_mont INTEGER,
    remise DECIMAL(24,6),
    prix_ven_nouv DECIMAL(24,6),
    remise_nouv DECIMAL(24,6)
);

-- Create indexes
CREATE INDEX idx_products_customer_product ON products_customer(reference_product);
CREATE INDEX idx_products_customer_customer ON products_customer(reference_customer); 