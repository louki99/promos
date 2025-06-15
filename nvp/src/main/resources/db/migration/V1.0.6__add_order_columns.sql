-- Add new columns for B2B and B2C specific features
ALTER TABLE orders
    -- B2B Credit Management
    ADD COLUMN credit_limit DECIMAL(19,2),
    ADD COLUMN available_credit DECIMAL(19,2),
    ADD COLUMN payment_due_days INTEGER,
    ADD COLUMN credit_terms VARCHAR(255),

    -- Bulk Order Management
    ADD COLUMN is_bulk_order BOOLEAN DEFAULT FALSE,
    ADD COLUMN bulk_order_reference VARCHAR(255),
    ADD COLUMN bulk_order_notes TEXT,
    ADD COLUMN scheduled_delivery_date TIMESTAMP WITH TIME ZONE,
    ADD COLUMN delivery_schedule_frequency VARCHAR(50),

    -- Contract Management
    ADD COLUMN contract_id VARCHAR(255),
    ADD COLUMN contract_start_date TIMESTAMP WITH TIME ZONE,
    ADD COLUMN contract_end_date TIMESTAMP WITH TIME ZONE,
    ADD COLUMN contract_terms TEXT,
    ADD COLUMN special_pricing_terms TEXT,

    -- Delivery Time Slot Management
    ADD COLUMN preferred_delivery_time_slot VARCHAR(50),
    ADD COLUMN delivery_time_slot_confirmed BOOLEAN DEFAULT FALSE,
    ADD COLUMN contact_phone VARCHAR(20),
    ADD COLUMN contact_email VARCHAR(255),

    -- Loyalty Program
    ADD COLUMN loyalty_points_earned INTEGER DEFAULT 0,
    ADD COLUMN loyalty_points_redeemed INTEGER DEFAULT 0,
    ADD COLUMN loyalty_discount_applied DECIMAL(19,2) DEFAULT 0,
    ADD COLUMN loyalty_member_id VARCHAR(255);

-- Add indexes for frequently queried columns
CREATE INDEX idx_orders_bulk_order ON orders(is_bulk_order);
CREATE INDEX idx_orders_contract_id ON orders(contract_id);
CREATE INDEX idx_orders_scheduled_delivery_date ON orders(scheduled_delivery_date);
CREATE INDEX idx_orders_loyalty_member_id ON orders(loyalty_member_id);

-- Add comments to columns for better documentation
COMMENT ON COLUMN orders.credit_limit IS 'Maximum credit limit for B2B customers';
COMMENT ON COLUMN orders.available_credit IS 'Available credit for B2B customers';
COMMENT ON COLUMN orders.payment_due_days IS 'Number of days until payment is due';
COMMENT ON COLUMN orders.credit_terms IS 'Terms and conditions for credit';
COMMENT ON COLUMN orders.is_bulk_order IS 'Indicates if this is a bulk order';
COMMENT ON COLUMN orders.bulk_order_reference IS 'Reference number for bulk orders';
COMMENT ON COLUMN orders.scheduled_delivery_date IS 'Scheduled delivery date for bulk orders';
COMMENT ON COLUMN orders.delivery_schedule_frequency IS 'Frequency of delivery for bulk orders (DAILY, WEEKLY, MONTHLY)';
COMMENT ON COLUMN orders.contract_id IS 'Contract identifier for B2B orders';
COMMENT ON COLUMN orders.contract_start_date IS 'Start date of the contract';
COMMENT ON COLUMN orders.contract_end_date IS 'End date of the contract';
COMMENT ON COLUMN orders.contract_terms IS 'Terms and conditions of the contract';
COMMENT ON COLUMN orders.special_pricing_terms IS 'Special pricing terms for B2B orders';
COMMENT ON COLUMN orders.preferred_delivery_time_slot IS 'Preferred delivery time slot for B2C orders';
COMMENT ON COLUMN orders.delivery_time_slot_confirmed IS 'Indicates if delivery time slot is confirmed';
COMMENT ON COLUMN orders.contact_phone IS 'Contact phone number for B2C orders';
COMMENT ON COLUMN orders.contact_email IS 'Contact email for B2C orders';
COMMENT ON COLUMN orders.loyalty_points_earned IS 'Loyalty points earned from this order';
COMMENT ON COLUMN orders.loyalty_points_redeemed IS 'Loyalty points redeemed in this order';
COMMENT ON COLUMN orders.loyalty_discount_applied IS 'Discount amount from loyalty points';
COMMENT ON COLUMN orders.loyalty_member_id IS 'Loyalty program member identifier'; 