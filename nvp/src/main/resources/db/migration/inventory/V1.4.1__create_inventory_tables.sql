-- Create product stocks table
CREATE TABLE product_stocks (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(500),
    depot_id BIGINT NOT NULL REFERENCES depots(id),
    depot_name VARCHAR(100),
    quantity DECIMAL(24,6) NOT NULL,
    reserved_quantity DECIMAL(24,6) NOT NULL DEFAULT 0,
    unit_cost DECIMAL(24,6) NOT NULL,
    expiry_date DATE,
    quality_status quality_status NOT NULL DEFAULT 'INSPECTED',
    quality_notes VARCHAR(1000),
    
    -- Stock level management
    min_stock_level DECIMAL(24,6),
    max_stock_level DECIMAL(24,6),
    reorder_point DECIMAL(24,6),
    reorder_quantity DECIMAL(24,6),
    shelf_life_days INTEGER,
    storage_conditions VARCHAR(100),
    days_of_stock INTEGER,
    
    -- Stock tracking
    batch_number VARCHAR(50),
    location_code VARCHAR(50),
    last_purchase_date TIMESTAMP WITH TIME ZONE,
    last_sale_date TIMESTAMP WITH TIME ZONE,
    notes VARCHAR(500),
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_product_stocks_product_id ON product_stocks(product_id);
CREATE INDEX idx_product_stocks_depot_id ON product_stocks(depot_id);
CREATE INDEX idx_product_stocks_expiry_date ON product_stocks(expiry_date);
CREATE INDEX idx_product_stocks_quality_status ON product_stocks(quality_status);
CREATE INDEX idx_product_stocks_batch_number ON product_stocks(batch_number);
CREATE INDEX idx_product_stocks_location_code ON product_stocks(location_code); 