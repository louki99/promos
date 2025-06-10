-- Create customer tables
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    ct_num VARCHAR(255) NOT NULL UNIQUE,
    ice VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    telephone VARCHAR(50),
    telecopie VARCHAR(50),
    email VARCHAR(255),
    address TEXT,
    code_postal VARCHAR(20),
    ville VARCHAR(100),
    country VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT true,
    cate_tarif_id BIGINT REFERENCES cat_tarif(id),
    num_payeur JSONB,
    max_credit DECIMAL(24,6),
    current_credit DECIMAL(24,6),
    payment_term_days INTEGER,
    tax_id VARCHAR(50),
    registration_number VARCHAR(50),
    is_vip BOOLEAN NOT NULL DEFAULT false,
    loyalty_points INTEGER DEFAULT 0,
    last_order_date TIMESTAMP WITH TIME ZONE,
    total_orders INTEGER DEFAULT 0,
    total_spent DECIMAL(24,6) DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_group_members (
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    group_id BIGINT NOT NULL REFERENCES customer_groups(id) ON DELETE CASCADE,
    PRIMARY KEY (customer_id, group_id)
);

-- Create indexes
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_phone ON customers(telephone); 