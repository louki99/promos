-- Add B2B and B2C specific fields to products table
ALTER TABLE products
    -- B2B specific fields
    ADD COLUMN b2b_minimum_order_value DECIMAL(19,2),
    ADD COLUMN b2b_contract_required BOOLEAN DEFAULT FALSE,
    ADD COLUMN b2b_credit_terms INTEGER,
    ADD COLUMN b2b_payment_methods VARCHAR(255),
    ADD COLUMN b2b_delivery_lead_time INTEGER,
    ADD COLUMN b2b_custom_pricing_enabled BOOLEAN DEFAULT FALSE,
    ADD COLUMN b2b_volume_discount_enabled BOOLEAN DEFAULT FALSE,
    ADD COLUMN b2b_contract_discount_percentage DECIMAL(5,2),
    
    -- B2C specific fields
    ADD COLUMN b2c_retail_price DECIMAL(19,2),
    ADD COLUMN b2c_promo_price DECIMAL(19,2),
    ADD COLUMN b2c_promo_start_date TIMESTAMP WITH TIME ZONE,
    ADD COLUMN b2c_promo_end_date TIMESTAMP WITH TIME ZONE,
    ADD COLUMN b2c_loyalty_points_multiplier DECIMAL(5,2) DEFAULT 1.00,
    ADD COLUMN b2c_display_in_catalog BOOLEAN DEFAULT TRUE,
    ADD COLUMN b2c_featured BOOLEAN DEFAULT FALSE,
    ADD COLUMN b2c_rating DECIMAL(3,2),
    ADD COLUMN b2c_review_count INTEGER DEFAULT 0,
    
    -- Common fields
    ADD COLUMN target_market VARCHAR(50) DEFAULT 'BOTH',
    ADD COLUMN customer_segments VARCHAR(255),
    ADD COLUMN seasonality VARCHAR(50),
    ADD COLUMN availability_schedule JSONB,
    ADD COLUMN custom_attributes JSONB;

-- Add indexes for frequently queried columns
CREATE INDEX idx_products_target_market ON products(target_market);
CREATE INDEX idx_products_b2c_featured ON products(b2c_featured) WHERE b2c_featured = TRUE;
CREATE INDEX idx_products_b2c_display ON products(b2c_display_in_catalog) WHERE b2c_display_in_catalog = TRUE;
CREATE INDEX idx_products_b2b_contract ON products(b2b_contract_required) WHERE b2b_contract_required = TRUE;
CREATE INDEX IF NOT EXISTS idx_products_b2c_display_in_catalog ON products(b2c_display_in_catalog);
CREATE INDEX IF NOT EXISTS idx_products_b2c_rating ON products(b2c_rating);
CREATE INDEX IF NOT EXISTS idx_products_seasonality ON products(seasonality); 