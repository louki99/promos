-- Add customer type
ALTER TABLE customers ADD COLUMN customer_type VARCHAR(10) NOT NULL DEFAULT 'B2C';

-- Add B2B specific fields
ALTER TABLE customers ADD COLUMN company_name VARCHAR(200);
ALTER TABLE customers ADD COLUMN legal_form VARCHAR(50);
ALTER TABLE customers ADD COLUMN registration_number VARCHAR(50);
ALTER TABLE customers ADD COLUMN tax_id VARCHAR(50);
ALTER TABLE customers ADD COLUMN vat_number VARCHAR(50);
ALTER TABLE customers ADD COLUMN business_activity VARCHAR(200);
ALTER TABLE customers ADD COLUMN annual_turnover DECIMAL(24,6);
ALTER TABLE customers ADD COLUMN number_of_employees INTEGER;

-- Add B2B contract information
ALTER TABLE customers ADD COLUMN contract_number VARCHAR(50);
ALTER TABLE customers ADD COLUMN contract_start_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE customers ADD COLUMN contract_end_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE customers ADD COLUMN contract_type VARCHAR(50);
ALTER TABLE customers ADD COLUMN contract_terms TEXT;
ALTER TABLE customers ADD COLUMN payment_terms TEXT;
ALTER TABLE customers ADD COLUMN delivery_terms TEXT;
ALTER TABLE customers ADD COLUMN special_conditions TEXT;

-- Add B2B credit information
ALTER TABLE customers ADD COLUMN credit_score INTEGER;
ALTER TABLE customers ADD COLUMN payment_history TEXT;

-- Create indexes for B2B fields
CREATE INDEX idx_customers_company_name ON customers(company_name);
CREATE INDEX idx_customers_tax_id ON customers(tax_id);
CREATE INDEX idx_customers_vat_number ON customers(vat_number);
CREATE INDEX idx_customers_contract_number ON customers(contract_number);
CREATE INDEX idx_customers_customer_type ON customers(customer_type); 