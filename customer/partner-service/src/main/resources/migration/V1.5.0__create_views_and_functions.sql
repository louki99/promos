-- Migration: Create Views and Functions for Analytics
-- Description: Creates database views and functions for common queries and analytics
-- Version: 1.5.0
-- Date: 2024-01-15

-- View: Active Partners Summary
CREATE VIEW v_active_partners_summary AS
SELECT 
    partner_type,
    COUNT(*) as total_count,
    COUNT(CASE WHEN is_vip THEN 1 END) as vip_count,
    AVG(total_spent) as avg_total_spent,
    SUM(total_spent) as total_revenue,
    AVG(loyalty_points) as avg_loyalty_points
FROM partners 
WHERE is_active = true
GROUP BY partner_type;

-- View: B2B Partners with Expiring Contracts
CREATE VIEW v_b2b_expiring_contracts AS
SELECT 
    id,
    ct_num,
    company_name,
    contract_number,
    contract_end_date,
    EXTRACT(DAY FROM (contract_end_date - CURRENT_TIMESTAMP)) as days_until_expiry
FROM partners 
WHERE partner_type = 'B2B' 
    AND contract_end_date IS NOT NULL 
    AND contract_end_date > CURRENT_TIMESTAMP
    AND contract_end_date <= CURRENT_TIMESTAMP + INTERVAL '90 days'
    AND is_active = true
ORDER BY contract_end_date;

-- View: Partners with Overdue Payments
CREATE VIEW v_partners_overdue_payments AS
SELECT 
    id,
    ct_num,
    partner_type,
    COALESCE(company_name, description) as partner_name,
    outstanding_balance,
    credit_limit,
    (outstanding_balance / NULLIF(credit_limit, 0)) * 100 as utilization_percentage
FROM partners 
WHERE outstanding_balance > 0 
    AND credit_limit > 0
    AND is_active = true
ORDER BY outstanding_balance DESC;

-- View: Top Spending Partners
CREATE VIEW v_top_spending_partners AS
SELECT 
    id,
    ct_num,
    partner_type,
    COALESCE(company_name, description) as partner_name,
    total_spent,
    total_orders,
    average_order_value,
    loyalty_points,
    is_vip
FROM partners 
WHERE is_active = true
ORDER BY total_spent DESC;

-- View: Partner Loyalty Levels
CREATE VIEW v_partner_loyalty_levels AS
SELECT 
    id,
    ct_num,
    partner_type,
    COALESCE(company_name, description) as partner_name,
    loyalty_points,
    total_spent,
    CASE 
        WHEN total_spent >= 10000 THEN 5
        WHEN total_spent >= 5000 THEN 4
        WHEN total_spent >= 2000 THEN 3
        WHEN total_spent >= 500 THEN 2
        WHEN total_spent >= 100 THEN 1
        ELSE 0
    END as loyalty_level
FROM partners 
WHERE is_active = true
ORDER BY loyalty_level DESC, total_spent DESC;

-- Function: Get Partner Statistics
CREATE OR REPLACE FUNCTION get_partner_statistics()
RETURNS TABLE (
    total_partners BIGINT,
    active_partners BIGINT,
    b2b_partners BIGINT,
    b2c_partners BIGINT,
    vip_partners BIGINT,
    total_revenue DECIMAL,
    avg_order_value DECIMAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        COUNT(*) as total_partners,
        COUNT(CASE WHEN is_active THEN 1 END) as active_partners,
        COUNT(CASE WHEN partner_type = 'B2B' THEN 1 END) as b2b_partners,
        COUNT(CASE WHEN partner_type = 'B2C' THEN 1 END) as b2c_partners,
        COUNT(CASE WHEN is_vip THEN 1 END) as vip_partners,
        SUM(total_spent) as total_revenue,
        AVG(average_order_value) as avg_order_value
    FROM partners;
END;
$$ LANGUAGE plpgsql;

-- Function: Get Partners by Credit Rating
CREATE OR REPLACE FUNCTION get_partners_by_credit_rating(rating VARCHAR)
RETURNS TABLE (
    id BIGINT,
    ct_num VARCHAR,
    partner_type VARCHAR,
    partner_name TEXT,
    credit_rating VARCHAR,
    credit_score INTEGER,
    credit_limit DECIMAL,
    outstanding_balance DECIMAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        p.id,
        p.ct_num,
        p.partner_type,
        COALESCE(p.company_name, p.description) as partner_name,
        p.credit_rating,
        p.credit_score,
        p.credit_limit,
        p.outstanding_balance
    FROM partners p
    WHERE p.credit_rating = rating
        AND p.is_active = true
    ORDER BY p.credit_score DESC;
END;
$$ LANGUAGE plpgsql;

-- Function: Get Partners by Annual Turnover Range
CREATE OR REPLACE FUNCTION get_partners_by_turnover_range(min_turnover DECIMAL, max_turnover DECIMAL)
RETURNS TABLE (
    id BIGINT,
    ct_num VARCHAR,
    company_name VARCHAR,
    annual_turnover DECIMAL,
    number_of_employees INTEGER,
    contract_number VARCHAR,
    contract_end_date TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        p.id,
        p.ct_num,
        p.company_name,
        p.annual_turnover,
        p.number_of_employees,
        p.contract_number,
        p.contract_end_date
    FROM partners p
    WHERE p.partner_type = 'B2B'
        AND p.annual_turnover BETWEEN min_turnover AND max_turnover
        AND p.is_active = true
    ORDER BY p.annual_turnover DESC;
END;
$$ LANGUAGE plpgsql;

-- Function: Get Partners by Age Range (B2C only)
CREATE OR REPLACE FUNCTION get_partners_by_age_range(min_age INTEGER, max_age INTEGER)
RETURNS TABLE (
    id BIGINT,
    ct_num VARCHAR,
    description TEXT,
    date_of_birth VARCHAR,
    age_years INTEGER,
    preferred_language VARCHAR,
    marketing_consent BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        p.id,
        p.ct_num,
        p.description,
        p.date_of_birth,
        EXTRACT(YEAR FROM AGE(CURRENT_DATE, p.date_of_birth::DATE)) as age_years,
        p.preferred_language,
        p.marketing_consent
    FROM partners p
    WHERE p.partner_type = 'B2C'
        AND p.date_of_birth IS NOT NULL
        AND EXTRACT(YEAR FROM AGE(CURRENT_DATE, p.date_of_birth::DATE)) BETWEEN min_age AND max_age
        AND p.is_active = true
    ORDER BY age_years;
END;
$$ LANGUAGE plpgsql;

-- Function: Update Partner Activity
CREATE OR REPLACE FUNCTION update_partner_activity(partner_id BIGINT)
RETURNS VOID AS $$
BEGIN
    UPDATE partners 
    SET last_activity_date = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = partner_id;
END;
$$ LANGUAGE plpgsql;

-- Function: Calculate Partner Loyalty Level
CREATE OR REPLACE FUNCTION calculate_loyalty_level(total_spent DECIMAL)
RETURNS INTEGER AS $$
BEGIN
    RETURN CASE 
        WHEN total_spent >= 10000 THEN 5
        WHEN total_spent >= 5000 THEN 4
        WHEN total_spent >= 2000 THEN 3
        WHEN total_spent >= 500 THEN 2
        WHEN total_spent >= 100 THEN 1
        ELSE 0
    END;
END;
$$ LANGUAGE plpgsql; 