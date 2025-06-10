-- Add missing columns to customers table
ALTER TABLE customers
    ADD COLUMN IF NOT EXISTS loyalty_points INTEGER DEFAULT 0,
    ADD COLUMN IF NOT EXISTS total_orders INTEGER DEFAULT 0,
    ADD COLUMN IF NOT EXISTS total_spent DECIMAL(19,2) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS average_order_value DECIMAL(19,2) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS last_order_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS credit_score INTEGER DEFAULT 0,
    ADD COLUMN IF NOT EXISTS credit_limit DECIMAL(19,2) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS outstanding_balance DECIMAL(19,2) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS business_activity VARCHAR(255),
    ADD COLUMN IF NOT EXISTS annual_turnover DECIMAL(19,2) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS contract_start_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS contract_end_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS is_vip BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add indexes for frequently queried columns
CREATE INDEX IF NOT EXISTS idx_customers_loyalty_points ON customers(loyalty_points);
CREATE INDEX IF NOT EXISTS idx_customers_total_spent ON customers(total_spent);
CREATE INDEX IF NOT EXISTS idx_customers_is_vip ON customers(is_vip);
CREATE INDEX IF NOT EXISTS idx_customers_active ON customers(active);
CREATE INDEX IF NOT EXISTS idx_customers_contract_end_date ON customers(contract_end_date);
CREATE INDEX IF NOT EXISTS idx_customers_business_activity ON customers(business_activity);
CREATE INDEX IF NOT EXISTS idx_customers_annual_turnover ON customers(annual_turnover); 