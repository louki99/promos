-- Create product families table
CREATE TABLE product_families (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    level INTEGER NOT NULL,
    parent_id BIGINT REFERENCES categories(id),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    reference VARCHAR(50) NOT NULL UNIQUE,
    sku VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    description TEXT NOT NULL,
    barcode VARCHAR(36) UNIQUE,
    family_code VARCHAR(50) REFERENCES product_families(code),
    
    -- Pricing
    sale_price DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    sale_unit VARCHAR(50),
    price_including_tax DECIMAL(10,2) NOT NULL,
    promo_sku_points DECIMAL(10,2) NOT NULL DEFAULT 0,
    tax_rate DECIMAL(5,2) DEFAULT 0,
    wholesale_price DECIMAL(10,2),
    minimum_order_quantity INTEGER DEFAULT 1,
    
    -- Operational flags
    deliverable BOOLEAN NOT NULL DEFAULT false,
    vendable BOOLEAN NOT NULL DEFAULT true,
    visible BOOLEAN NOT NULL DEFAULT true,
    inactive BOOLEAN NOT NULL DEFAULT false,
    requires_approval BOOLEAN NOT NULL DEFAULT false,
    is_bulk_item BOOLEAN NOT NULL DEFAULT false,
    is_perishable BOOLEAN NOT NULL DEFAULT false,
    requires_cold_storage BOOLEAN NOT NULL DEFAULT false,
    
    -- Stock tracking
    stock_tracking suivi_stock NOT NULL DEFAULT 'Aucun',
    
    -- Product attributes
    weight DECIMAL(10,3),
    weight_unit VARCHAR(10),
    dimensions VARCHAR(50), -- Format: "LxWxH" in cm
    package_size VARCHAR(50),
    package_quantity INTEGER,
    supplier_code VARCHAR(50),
    supplier_name VARCHAR(100),
    country_of_origin VARCHAR(100),
    certification VARCHAR(200), -- e.g., "HALAL", "ORGANIC", etc.
    
    -- Images and media
    photo VARCHAR(500),
    additional_photos TEXT[], -- Array of photo URLs
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create product categories junction table
CREATE TABLE product_categories (
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, category_id)
);

-- Create indexes
CREATE INDEX idx_products_family_code ON products(family_code);
CREATE INDEX idx_products_barcode ON products(barcode);
CREATE INDEX idx_products_vendable ON products(vendable);
CREATE INDEX idx_products_visible ON products(visible);
CREATE INDEX idx_products_supplier_code ON products(supplier_code);
CREATE INDEX idx_products_is_perishable ON products(is_perishable);
CREATE INDEX idx_products_requires_cold_storage ON products(requires_cold_storage);
CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_product_categories_product_id ON product_categories(product_id);
CREATE INDEX idx_product_categories_category_id ON product_categories(category_id);