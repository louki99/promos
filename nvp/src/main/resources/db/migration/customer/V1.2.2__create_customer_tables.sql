-- Create customer groups table
CREATE TABLE customer_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    ct_num VARCHAR(50) NOT NULL UNIQUE,
    ice VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    
    -- Contact Information
    telephone VARCHAR(20),
    telecopie VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    region VARCHAR(100),
    postal_code VARCHAR(20),
    
    -- B2B Specific Information
    company_name VARCHAR(200),
    legal_form VARCHAR(50),
    registration_number VARCHAR(50),
    tax_id VARCHAR(50),
    vat_number VARCHAR(50),
    business_activity VARCHAR(200),
    annual_turnover DECIMAL(24,6),
    number_of_employees INTEGER,
    
    -- Financial Information
    cate_tarif_id BIGINT REFERENCES cat_tarif(id),
    num_payeur JSONB,
    max_credit DECIMAL(24,6),
    current_credit DECIMAL(24,6),
    payment_term_days INTEGER,
    preferred_payment_method VARCHAR(50),
    bank_account_info TEXT,
    credit_rating VARCHAR(20),
    credit_limit DECIMAL(24,6),
    outstanding_balance DECIMAL(24,6),
    last_payment_date TIMESTAMP WITH TIME ZONE,
    payment_history TEXT,
    
    -- Business Status
    is_vip BOOLEAN NOT NULL DEFAULT false,
    loyalty_points INTEGER DEFAULT 0,
    last_order_date TIMESTAMP WITH TIME ZONE,
    total_orders INTEGER DEFAULT 0,
    total_spent DECIMAL(24,6) DEFAULT 0,
    average_order_value DECIMAL(24,6),
    customer_since TIMESTAMP WITH TIME ZONE,
    preferred_delivery_time VARCHAR(50),
    preferred_delivery_days VARCHAR(100),
    special_handling_instructions TEXT,
    
    -- Audit
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    last_activity_date TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create customer group members junction table
CREATE TABLE customer_group_members (
    customer_id BIGINT REFERENCES customers(id) ON DELETE CASCADE,
    group_id BIGINT REFERENCES customer_groups(id) ON DELETE CASCADE,
    PRIMARY KEY (customer_id, group_id)
);

-- Create indexes
CREATE INDEX idx_customers_ct_num ON customers(ct_num);
CREATE INDEX idx_customers_ice ON customers(ice);
CREATE INDEX idx_customers_company_name ON customers(company_name);
CREATE INDEX idx_customers_tax_id ON customers(tax_id);
CREATE INDEX idx_customers_vat_number ON customers(vat_number);
CREATE INDEX idx_customers_is_vip ON customers(is_vip);
CREATE INDEX idx_customers_is_active ON customers(is_active);
CREATE INDEX idx_customers_last_order_date ON customers(last_order_date);
CREATE INDEX idx_customer_groups_name ON customer_groups(name); 