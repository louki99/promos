-- Migration: Insert Sample Data
-- Description: Inserts sample B2B and B2C partners for testing and demonstration
-- Version: 1.4.0
-- Date: 2024-01-15

-- Sample B2B Partners
INSERT INTO partners (
    ct_num, ice, description, partner_type, category_tarif_id,
    telephone, email, address, city, country, postal_code,
    credit_limit, credit_rating, credit_score, outstanding_balance, payment_term_days,
    is_vip, loyalty_points, total_orders, total_spent, average_order_value, partner_since,
    preferred_delivery_time, preferred_delivery_days,
    notes, is_active, created_by,
    company_name, legal_form, registration_number, tax_id, vat_number, business_activity, annual_turnover, number_of_employees,
    contract_number, contract_start_date, contract_end_date, contract_type, contract_terms, payment_terms, delivery_terms
) VALUES
-- B2B Partner 1: Large Corporation
(
    'CT001', 'ICE001', 'Acme Corporation - Large Manufacturing Company', 'B2B', 1,
    '+212-5-22-123456', 'contact@acme.com', '123 Business District', 'Casablanca', 'Morocco', '20000',
    1000000.00, 'A+', 850, 150000.00, 30,
    true, 5000, 150, 2500000.00, 16666.67, '2020-01-15 00:00:00+00',
    '09:00-17:00', 'Monday,Wednesday,Friday',
    'Premium B2B partner with excellent payment history', true, 'system',
    'Acme Corporation', 'SARL', 'RC123456', 'TAX001', 'VAT001', 'Manufacturing', 50000000.00, 500,
    'CON-2020-001', '2020-01-15 00:00:00+00', '2025-01-15 00:00:00+00', 'Standard', 'Standard terms and conditions', 'Net 30 days', 'Standard delivery'
),
-- B2B Partner 2: Medium Business
(
    'CT002', 'ICE002', 'Tech Solutions Ltd - IT Services Provider', 'B2B', 2,
    '+212-5-22-234567', 'info@techsolutions.com', '456 Tech Park', 'Rabat', 'Morocco', '10000',
    500000.00, 'A', 750, 75000.00, 45,
    false, 2500, 75, 1200000.00, 16000.00, '2021-03-20 00:00:00+00',
    '10:00-16:00', 'Tuesday,Thursday',
    'Reliable IT services partner', true, 'system',
    'Tech Solutions Ltd', 'SARL', 'RC234567', 'TAX002', 'VAT002', 'IT Services', 15000000.00, 50,
    'CON-2021-002', '2021-03-20 00:00:00+00', '2024-03-20 00:00:00+00', 'Premium', 'Premium service terms', 'Net 45 days', 'Express delivery'
),
-- B2B Partner 3: Small Business
(
    'CT003', 'ICE003', 'Local Restaurant Chain - Food Service', 'B2B', 3,
    '+212-5-22-345678', 'orders@localrestaurant.com', '789 Food Street', 'Marrakech', 'Morocco', '40000',
    100000.00, 'B+', 650, 25000.00, 15,
    false, 1000, 200, 800000.00, 4000.00, '2022-06-10 00:00:00+00',
    '06:00-12:00', 'Monday,Tuesday,Wednesday,Thursday,Friday',
    'Local restaurant chain with daily orders', true, 'system',
    'Local Restaurant Chain', 'SARL', 'RC345678', 'TAX003', 'VAT003', 'Food Service', 5000000.00, 25,
    'CON-2022-003', '2022-06-10 00:00:00+00', '2023-06-10 00:00:00+00', 'Basic', 'Basic service terms', 'Net 15 days', 'Daily delivery'
);

-- Sample B2C Partners
INSERT INTO partners (
    ct_num, ice, description, partner_type, category_tarif_id,
    telephone, email, address, city, country, postal_code,
    credit_limit, credit_rating, credit_score, outstanding_balance, payment_term_days,
    is_vip, loyalty_points, total_orders, total_spent, average_order_value, partner_since,
    preferred_delivery_time, preferred_delivery_days,
    notes, is_active, created_by,
    personal_id_number, date_of_birth, preferred_language, marketing_consent
) VALUES
-- B2C Partner 1: VIP Customer
(
    'CT004', 'ICE004', 'Ahmed Ben Ali - Premium Customer', 'B2C', 1,
    '+212-6-12-456789', 'ahmed.benali@email.com', '321 Residential Area', 'Casablanca', 'Morocco', '20000',
    50000.00, 'A+', 800, 5000.00, 0,
    true, 8000, 120, 150000.00, 1250.00, '2019-08-15 00:00:00+00',
    '18:00-20:00', 'Monday,Wednesday,Friday,Saturday',
    'VIP partner with excellent loyalty', true, 'system',
    'ID123456789', '1985-03-15', 'ar', true
),
-- B2C Partner 2: Regular Customer
(
    'CT005', 'ICE005', 'Fatima Zahra - Regular Customer', 'B2C', 2,
    '+212-6-12-567890', 'fatima.zahra@email.com', '654 Home Street', 'Rabat', 'Morocco', '10000',
    10000.00, 'B', 600, 1500.00, 0,
    false, 2500, 45, 35000.00, 777.78, '2020-11-20 00:00:00+00',
    '19:00-21:00', 'Tuesday,Thursday,Sunday',
    'Regular partner with good payment history', true, 'system',
    'ID234567890', '1990-07-22', 'fr', true
),
-- B2C Partner 3: New Customer
(
    'CT006', 'ICE006', 'Youssef Alami - New Customer', 'B2C', 3,
    '+212-6-12-678901', 'youssef.alami@email.com', '987 New District', 'Marrakech', 'Morocco', '40000',
    2000.00, 'C', 400, 0.00, 0,
    false, 100, 5, 2500.00, 500.00, '2023-01-10 00:00:00+00',
    '20:00-22:00', 'Friday,Saturday',
    'New partner with limited history', true, 'system',
    'ID345678901', '1995-12-08', 'ar', false
),
-- B2C Partner 4: Inactive Customer
(
    'CT007', 'ICE007', 'Amina Tazi - Inactive Customer', 'B2C', 2,
    '+212-6-12-789012', 'amina.tazi@email.com', '147 Old Quarter', 'Fez', 'Morocco', '30000',
    5000.00, 'B-', 500, 0.00, 0,
    false, 1500, 30, 20000.00, 666.67, '2020-05-12 00:00:00+00',
    '17:00-19:00', 'Monday,Wednesday',
    'Inactive partner - no recent orders', false, 'system',
    'ID456789012', '1988-09-30', 'fr', false
);

-- Assign partners to groups
INSERT INTO partner_group_members (partner_id, group_id, created_at) VALUES
-- B2B Partners to groups
(1, 3, CURRENT_TIMESTAMP), -- Acme Corp to Wholesale Partners
(1, 1, CURRENT_TIMESTAMP), -- Acme Corp to VIP Partners
(2, 3, CURRENT_TIMESTAMP), -- Tech Solutions to Wholesale Partners
(3, 2, CURRENT_TIMESTAMP), -- Local Restaurant to Regular Customers

-- B2C Partners to groups
(4, 1, CURRENT_TIMESTAMP), -- Ahmed to VIP Customers
(5, 2, CURRENT_TIMESTAMP), -- Fatima to Regular Customers
(6, 4, CURRENT_TIMESTAMP), -- Youssef to New Customers
(7, 5, CURRENT_TIMESTAMP); -- Amina to Inactive Customers 