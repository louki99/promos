-- Add customer type
ALTER TABLE customers ADD COLUMN customer_type VARCHAR(10) NOT NULL DEFAULT 'B2C';

-- Add B2B contract information
ALTER TABLE customers ADD COLUMN contract_number VARCHAR(50);
ALTER TABLE customers ADD COLUMN contract_type VARCHAR(50);
ALTER TABLE customers ADD COLUMN contract_terms TEXT;
ALTER TABLE customers ADD COLUMN payment_terms TEXT;
ALTER TABLE customers ADD COLUMN delivery_terms TEXT;
ALTER TABLE customers ADD COLUMN special_conditions TEXT;

-- Create indexes for new B2B fields
CREATE INDEX idx_customers_contract_number ON customers(contract_number);
CREATE INDEX idx_customers_customer_type ON customers(customer_type); 