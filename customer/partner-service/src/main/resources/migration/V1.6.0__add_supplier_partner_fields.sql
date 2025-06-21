-- Migration: Add Supplier Partner Fields
-- Description: Adds supplier-specific fields to the partners table and updates partner_type constraint
-- Version: 1.6.0
-- Date: 2024-01-20

-- Update partner_type constraint to include SUPPLIER
ALTER TABLE partners DROP CONSTRAINT IF EXISTS partners_partner_type_check;
ALTER TABLE partners ADD CONSTRAINT partners_partner_type_check 
    CHECK (partner_type IN ('B2B', 'B2C', 'SUPPLIER'));

-- Add supplier-specific fields
ALTER TABLE partners ADD COLUMN IF NOT EXISTS supplier_code VARCHAR(10) UNIQUE;
ALTER TABLE partners ADD COLUMN IF NOT EXISTS supplier_category VARCHAR(50);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS supplier_rating VARCHAR(1);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS delivery_performance_score DECIMAL(5,2);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS quality_score DECIMAL(5,2);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS price_competitiveness_score DECIMAL(5,2);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS payment_terms_days INTEGER;
ALTER TABLE partners ADD COLUMN IF NOT EXISTS minimum_order_amount DECIMAL(24,6);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS lead_time_days INTEGER;
ALTER TABLE partners ADD COLUMN IF NOT EXISTS certification_iso VARCHAR(50);
ALTER TABLE partners ADD COLUMN IF NOT EXISTS supplier_since TIMESTAMP WITH TIME ZONE;
ALTER TABLE partners ADD COLUMN IF NOT EXISTS last_audit_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE partners ADD COLUMN IF NOT EXISTS next_audit_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE partners ADD COLUMN IF NOT EXISTS supplier_status VARCHAR(20) DEFAULT 'PENDING_APPROVAL';
ALTER TABLE partners ADD COLUMN IF NOT EXISTS risk_level VARCHAR(10) DEFAULT 'MEDIUM';
ALTER TABLE partners ADD COLUMN IF NOT EXISTS supplier_notes TEXT;

-- Create indexes for supplier-specific queries
CREATE INDEX IF NOT EXISTS idx_partners_supplier_code ON partners(supplier_code);
CREATE INDEX IF NOT EXISTS idx_partners_supplier_category ON partners(supplier_category);
CREATE INDEX IF NOT EXISTS idx_partners_supplier_status ON partners(supplier_status);
CREATE INDEX IF NOT EXISTS idx_partners_risk_level ON partners(risk_level);
CREATE INDEX IF NOT EXISTS idx_partners_next_audit_date ON partners(next_audit_date);
CREATE INDEX IF NOT EXISTS idx_partners_supplier_rating ON partners(supplier_rating);
CREATE INDEX IF NOT EXISTS idx_partners_delivery_performance_score ON partners(delivery_performance_score);
CREATE INDEX IF NOT EXISTS idx_partners_quality_score ON partners(quality_score);
CREATE INDEX IF NOT EXISTS idx_partners_price_competitiveness_score ON partners(price_competitiveness_score);

-- Create composite indexes for common supplier queries
CREATE INDEX IF NOT EXISTS idx_partners_supplier_category_status ON partners(supplier_category, supplier_status);
CREATE INDEX IF NOT EXISTS idx_partners_supplier_risk_status ON partners(risk_level, supplier_status);
CREATE INDEX IF NOT EXISTS idx_partners_supplier_audit_dates ON partners(last_audit_date, next_audit_date);

-- Add constraints for supplier fields
ALTER TABLE partners ADD CONSTRAINT chk_supplier_rating_valid 
    CHECK (supplier_rating IS NULL OR supplier_rating IN ('A', 'B', 'C', 'D'));

ALTER TABLE partners ADD CONSTRAINT chk_supplier_category_valid 
    CHECK (supplier_category IS NULL OR supplier_category IN ('FOOD', 'BEVERAGE', 'PACKAGING', 'EQUIPMENT', 'SERVICES', 'OTHER'));

ALTER TABLE partners ADD CONSTRAINT chk_supplier_status_valid 
    CHECK (supplier_status IS NULL OR supplier_status IN ('ACTIVE', 'SUSPENDED', 'BLACKLISTED', 'PENDING_APPROVAL'));

ALTER TABLE partners ADD CONSTRAINT chk_risk_level_valid 
    CHECK (risk_level IS NULL OR risk_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'));

ALTER TABLE partners ADD CONSTRAINT chk_certification_iso_valid 
    CHECK (certification_iso IS NULL OR certification_iso IN ('ISO9001', 'ISO22000', 'HACCP', 'BRC', 'FSSC22000', 'OTHER'));

ALTER TABLE partners ADD CONSTRAINT chk_delivery_performance_score_range 
    CHECK (delivery_performance_score IS NULL OR (delivery_performance_score >= 0 AND delivery_performance_score <= 100));

ALTER TABLE partners ADD CONSTRAINT chk_quality_score_range 
    CHECK (quality_score IS NULL OR (quality_score >= 0 AND quality_score <= 100));

ALTER TABLE partners ADD CONSTRAINT chk_price_competitiveness_score_range 
    CHECK (price_competitiveness_score IS NULL OR (price_competitiveness_score >= 0 AND price_competitiveness_score <= 100));

ALTER TABLE partners ADD CONSTRAINT chk_payment_terms_days_range 
    CHECK (payment_terms_days IS NULL OR (payment_terms_days >= 0 AND payment_terms_days <= 365));

ALTER TABLE partners ADD CONSTRAINT chk_lead_time_days_range 
    CHECK (lead_time_days IS NULL OR (lead_time_days >= 1 AND lead_time_days <= 365));

ALTER TABLE partners ADD CONSTRAINT chk_minimum_order_amount_positive 
    CHECK (minimum_order_amount IS NULL OR minimum_order_amount >= 0);

-- Create a function to calculate overall supplier performance score
CREATE OR REPLACE FUNCTION calculate_supplier_performance_score(
    delivery_score DECIMAL,
    quality_score DECIMAL,
    price_score DECIMAL
) RETURNS DECIMAL AS $$
DECLARE
    total_score DECIMAL := 0;
    score_count INTEGER := 0;
BEGIN
    IF delivery_score IS NOT NULL THEN
        total_score := total_score + delivery_score;
        score_count := score_count + 1;
    END IF;
    
    IF quality_score IS NOT NULL THEN
        total_score := total_score + quality_score;
        score_count := score_count + 1;
    END IF;
    
    IF price_score IS NOT NULL THEN
        total_score := total_score + price_score;
        score_count := score_count + 1;
    END IF;
    
    IF score_count > 0 THEN
        RETURN ROUND(total_score / score_count, 2);
    ELSE
        RETURN NULL;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Create a view for supplier performance analysis
CREATE OR REPLACE VIEW supplier_performance_view AS
SELECT 
    id,
    ct_num,
    description,
    supplier_code,
    supplier_category,
    supplier_rating,
    supplier_status,
    risk_level,
    delivery_performance_score,
    quality_score,
    price_competitiveness_score,
    calculate_supplier_performance_score(
        delivery_performance_score,
        quality_score,
        price_competitiveness_score
    ) as overall_performance_score,
    supplier_since,
    last_audit_date,
    next_audit_date,
    CASE 
        WHEN next_audit_date IS NOT NULL AND next_audit_date < CURRENT_TIMESTAMP THEN 'OVERDUE'
        WHEN next_audit_date IS NOT NULL AND next_audit_date < CURRENT_TIMESTAMP + INTERVAL '30 days' THEN 'DUE_SOON'
        ELSE 'ON_SCHEDULE'
    END as audit_status,
    created_at,
    updated_at
FROM partners 
WHERE partner_type = 'SUPPLIER';

-- Create a view for supplier risk assessment
CREATE OR REPLACE VIEW supplier_risk_view AS
SELECT 
    id,
    ct_num,
    description,
    supplier_code,
    supplier_category,
    supplier_status,
    risk_level,
    overall_performance_score,
    CASE 
        WHEN risk_level = 'CRITICAL' OR supplier_status = 'BLACKLISTED' THEN 'IMMEDIATE_ACTION'
        WHEN risk_level = 'HIGH' OR supplier_status = 'SUSPENDED' THEN 'MONITOR_CLOSELY'
        WHEN overall_performance_score < 60 THEN 'PERFORMANCE_ISSUE'
        WHEN audit_status = 'OVERDUE' THEN 'AUDIT_OVERDUE'
        ELSE 'NORMAL'
    END as risk_assessment,
    audit_status,
    next_audit_date
FROM supplier_performance_view;

-- Add comments for documentation
COMMENT ON COLUMN partners.supplier_code IS 'Unique supplier code (3-10 alphanumeric characters)';
COMMENT ON COLUMN partners.supplier_category IS 'Supplier category: FOOD, BEVERAGE, PACKAGING, EQUIPMENT, SERVICES, OTHER';
COMMENT ON COLUMN partners.supplier_rating IS 'Supplier rating: A, B, C, D';
COMMENT ON COLUMN partners.delivery_performance_score IS 'Delivery performance score (0-100)';
COMMENT ON COLUMN partners.quality_score IS 'Quality score (0-100)';
COMMENT ON COLUMN partners.price_competitiveness_score IS 'Price competitiveness score (0-100)';
COMMENT ON COLUMN partners.payment_terms_days IS 'Payment terms in days (0-365)';
COMMENT ON COLUMN partners.minimum_order_amount IS 'Minimum order amount required';
COMMENT ON COLUMN partners.lead_time_days IS 'Lead time in days (1-365)';
COMMENT ON COLUMN partners.certification_iso IS 'ISO certification: ISO9001, ISO22000, HACCP, BRC, FSSC22000, OTHER';
COMMENT ON COLUMN partners.supplier_since IS 'Date when supplier relationship started';
COMMENT ON COLUMN partners.last_audit_date IS 'Date of last supplier audit';
COMMENT ON COLUMN partners.next_audit_date IS 'Date of next scheduled audit';
COMMENT ON COLUMN partners.supplier_status IS 'Supplier status: ACTIVE, SUSPENDED, BLACKLISTED, PENDING_APPROVAL';
COMMENT ON COLUMN partners.risk_level IS 'Risk level: LOW, MEDIUM, HIGH, CRITICAL';
COMMENT ON COLUMN partners.supplier_notes IS 'Additional notes about the supplier';

COMMENT ON VIEW supplier_performance_view IS 'View for analyzing supplier performance metrics';
COMMENT ON VIEW supplier_risk_view IS 'View for supplier risk assessment and monitoring'; 