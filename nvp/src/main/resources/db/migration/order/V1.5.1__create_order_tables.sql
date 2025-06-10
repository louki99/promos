-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    order_number VARCHAR(50) NOT NULL UNIQUE,
    reference_number VARCHAR(50),
    po_number VARCHAR(50),
    
    -- Order Status
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    payment_status VARCHAR(20),
    payment_method VARCHAR(20),
    
    -- Financial Information
    subtotal DECIMAL(24,6) NOT NULL DEFAULT 0,
    total_discount DECIMAL(24,6) NOT NULL DEFAULT 0,
    total_tax DECIMAL(24,6) NOT NULL DEFAULT 0,
    shipping_cost DECIMAL(24,6) NOT NULL DEFAULT 0,
    total DECIMAL(24,6) NOT NULL DEFAULT 0,
    
    -- Payment Details
    payment_due_date TIMESTAMP WITH TIME ZONE,
    payment_terms VARCHAR(100),
    payment_notes TEXT,
    payment_reference VARCHAR(100),
    
    -- Shipping Information
    shipping_address TEXT,
    shipping_city VARCHAR(100),
    shipping_country VARCHAR(100),
    shipping_postal_code VARCHAR(20),
    shipping_notes TEXT,
    preferred_delivery_date TIMESTAMP WITH TIME ZONE,
    delivery_instructions TEXT,
    
    -- B2B Specific
    is_wholesale BOOLEAN NOT NULL DEFAULT false,
    minimum_order_value DECIMAL(24,6),
    bulk_discount_percentage DECIMAL(5,2),
    special_pricing_agreement BOOLEAN DEFAULT false,
    contract_number VARCHAR(50),
    
    -- Order Details
    notes TEXT,
    internal_notes TEXT,
    cancellation_reason TEXT,
    refund_reason TEXT,
    
    -- Audit
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    refunded_at TIMESTAMP WITH TIME ZONE,
    delivered_at TIMESTAMP WITH TIME ZONE
);

-- Create order items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL,
    product_family_id BIGINT,
    product_name VARCHAR(200) NOT NULL,
    sku VARCHAR(50),
    sku_points DECIMAL(24,6),
    
    -- Pricing
    unit_price DECIMAL(24,6) NOT NULL,
    quantity INTEGER NOT NULL,
    discount_amount DECIMAL(24,6) NOT NULL DEFAULT 0,
    tax_amount DECIMAL(24,6) NOT NULL DEFAULT 0,
    total_price DECIMAL(24,6) NOT NULL,
    
    -- B2B Specific
    wholesale_price DECIMAL(24,6),
    bulk_quantity_threshold INTEGER,
    special_pricing BOOLEAN DEFAULT false,
    
    -- Promotion
    applied_promotion_code VARCHAR(50),
    consumed_quantity INTEGER NOT NULL DEFAULT 0,
    
    -- Additional Info
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create order item applied promotions table
CREATE TABLE order_item_applied_promotions (
    order_item_id BIGINT NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    promotion_code VARCHAR(50) NOT NULL,
    PRIMARY KEY (order_item_id, promotion_code)
);

-- Create order history table
CREATE TABLE order_history (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    created_by VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_is_wholesale ON orders(is_wholesale);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
CREATE INDEX idx_order_items_sku ON order_items(sku);
CREATE INDEX idx_order_history_order_id ON order_history(order_id);
CREATE INDEX idx_order_history_status ON order_history(status); 