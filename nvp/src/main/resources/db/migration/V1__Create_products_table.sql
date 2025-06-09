CREATE TABLE products (
    id UUID PRIMARY KEY,
    reference VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(500),
    description TEXT NOT NULL,
    barcode VARCHAR(19) UNIQUE,
    family_code VARCHAR(100),
    category1 VARCHAR(100),
    category2 VARCHAR(100),
    category3 VARCHAR(100),
    category4 VARCHAR(100),
    sale_price DECIMAL(10,2) NOT NULL,
    sale_unit VARCHAR(50),
    price_including_tax DECIMAL(10,2) NOT NULL,
    photo VARCHAR(500),
    deliverable BOOLEAN NOT NULL DEFAULT FALSE,
    inactive BOOLEAN NOT NULL DEFAULT FALSE,
    stock_tracking VARCHAR(20) NOT NULL DEFAULT 'Aucun',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_reference ON products(reference);
CREATE INDEX idx_products_barcode ON products(barcode);
CREATE INDEX idx_products_family_code ON products(family_code);
CREATE INDEX idx_products_deliverable ON products(deliverable);
CREATE INDEX idx_products_inactive ON products(inactive);