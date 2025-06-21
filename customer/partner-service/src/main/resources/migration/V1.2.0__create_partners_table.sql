-- Migration: Create Partners Table with Inheritance Structure
-- Description: Creates the partners table with SINGLE_TABLE inheritance for B2B and B2C partners
-- Version: 1.2.0
-- Date: 2024-01-15

CREATE TABLE partners (
    -- Primary key and basic fields
    id BIGSERIAL PRIMARY KEY,
    ct_num VARCHAR(255) NOT NULL UNIQUE,
    ice VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category_tarif_id BIGINT REFERENCES cat_tarif(id),
    
    -- Inheritance discriminator
    partner_type VARCHAR(10) NOT NULL CHECK (partner_type IN ('B2B', 'B2C')),
    
    -- Contact Information (Embedded)
    telephone VARCHAR(50),
    telecopie VARCHAR(50),
    email VARCHAR(255),
    address TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    region VARCHAR(100),
    postal_code VARCHAR(20),
    
    -- Credit Information (Embedded)
    credit_limit DECIMAL(24,6),
    credit_rating VARCHAR(50),
    credit_score INTEGER,
    payment_history TEXT,
    outstanding_balance DECIMAL(24,6) DEFAULT 0,
    last_payment_date TIMESTAMP WITH TIME ZONE,
    payment_term_days INTEGER,
    preferred_payment_method VARCHAR(100),
    bank_account_info TEXT,
    
    -- Loyalty Information (Embedded)
    is_vip BOOLEAN DEFAULT FALSE,
    loyalty_points INTEGER DEFAULT 0,
    total_orders INTEGER DEFAULT 0,
    total_spent DECIMAL(24,6) DEFAULT 0,
    average_order_value DECIMAL(24,6) DEFAULT 0,
    last_order_date TIMESTAMP WITH TIME ZONE,
    partner_since TIMESTAMP WITH TIME ZONE,
    
    -- Delivery Preference (Embedded)
    preferred_delivery_time VARCHAR(100),
    preferred_delivery_days VARCHAR(100),
    special_handling_instructions TEXT,
    
    -- Audit Information (Embedded)
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_activity_date TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- B2B-specific fields (Company Information)
    company_name VARCHAR(255),
    legal_form VARCHAR(100),
    registration_number VARCHAR(100),
    tax_id VARCHAR(100),
    vat_number VARCHAR(100),
    business_activity TEXT,
    annual_turnover DECIMAL(24,6),
    number_of_employees INTEGER,
    
    -- B2B-specific fields (Contract Information)
    contract_number VARCHAR(100),
    contract_start_date TIMESTAMP WITH TIME ZONE,
    contract_end_date TIMESTAMP WITH TIME ZONE,
    contract_type VARCHAR(100),
    contract_terms TEXT,
    payment_terms TEXT,
    delivery_terms TEXT,
    special_conditions TEXT,
    
    -- B2C-specific fields
    personal_id_number VARCHAR(50),
    date_of_birth VARCHAR(20),
    preferred_language VARCHAR(10),
    marketing_consent BOOLEAN DEFAULT FALSE
);

-- Create trigger for updating the updated_at column
CREATE TRIGGER update_partners_updated_at
    BEFORE UPDATE ON partners
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_column();

-- Create indexes for better performance
CREATE INDEX idx_partners_ct_num ON partners(ct_num);
CREATE INDEX idx_partners_ice ON partners(ice);
CREATE INDEX idx_partners_partner_type ON partners(partner_type);
CREATE INDEX idx_partners_is_active ON partners(is_active);
CREATE INDEX idx_partners_created_at ON partners(created_at);
CREATE INDEX idx_partners_email ON partners(email);
CREATE INDEX idx_partners_company_name ON partners(company_name);
CREATE INDEX idx_partners_contract_end_date ON partners(contract_end_date);
CREATE INDEX idx_partners_outstanding_balance ON partners(outstanding_balance);
CREATE INDEX idx_partners_total_spent ON partners(total_spent);
CREATE INDEX idx_partners_loyalty_points ON partners(loyalty_points);
CREATE INDEX idx_partners_personal_id_number ON partners(personal_id_number);

-- Create composite indexes for common queries
CREATE INDEX idx_partners_type_active ON partners(partner_type, is_active);
CREATE INDEX idx_partners_contract_dates ON partners(contract_start_date, contract_end_date);
CREATE INDEX idx_partners_credit_info ON partners(credit_limit, outstanding_balance);

-- Add constraints
ALTER TABLE partners ADD CONSTRAINT chk_credit_limit_positive CHECK (credit_limit >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_outstanding_balance_positive CHECK (outstanding_balance >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_loyalty_points_positive CHECK (loyalty_points >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_total_orders_positive CHECK (total_orders >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_total_spent_positive CHECK (total_spent >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_average_order_value_positive CHECK (average_order_value >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_contract_dates_valid CHECK (contract_start_date IS NULL OR contract_end_date IS NULL OR contract_end_date > contract_start_date);
ALTER TABLE partners ADD CONSTRAINT chk_annual_turnover_positive CHECK (annual_turnover >= 0);
ALTER TABLE partners ADD CONSTRAINT chk_number_of_employees_positive CHECK (number_of_employees >= 0); 