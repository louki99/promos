-- Migration: Create Partner Groups Table
-- Description: Creates the partner_groups table for managing partner groups
-- Version: 1.1.0
-- Date: 2024-01-15

CREATE TABLE partner_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create trigger for updating the updated_at column
CREATE TRIGGER update_partner_groups_updated_at
    BEFORE UPDATE ON partner_groups
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_column();

-- Create indexes for better performance
CREATE INDEX idx_partner_groups_name ON partner_groups(name);
CREATE INDEX idx_partner_groups_created_at ON partner_groups(created_at);

-- Insert default partner groups
INSERT INTO partner_groups (name, description, created_at, updated_at) VALUES
('VIP Customers', 'Very Important Partners with premium benefits', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Regular Customers', 'Standard customers with basic benefits', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wholesale Partners', 'Business partners with wholesale pricing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('New Customers', 'Recently registered customers', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Inactive Customers', 'Customers with no recent activity', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 