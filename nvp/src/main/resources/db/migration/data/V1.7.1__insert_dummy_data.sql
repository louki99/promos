-- Insert dummy data for testing

-- Insert sites
INSERT INTO sites (description, site_code, address_line1, city, country, postal_code, contact_phone, contact_email)
VALUES 
('Main Warehouse', 'WH001', '123 Industrial Zone', 'Casablanca', 'Morocco', '20000', '+212522000001', 'warehouse@foodplus.ma'),
('Branch Office', 'BR001', '45 Business District', 'Rabat', 'Morocco', '10000', '+212537000001', 'branch@foodplus.ma');

-- Insert depots
INSERT INTO depots (description, depot_code, site_id, depot_type, capacity_cubic_meters, temperature_range, is_refrigerated)
VALUES 
('Main Storage', 'DEP001', 1, 'MAIN', 1000.0, '15-25°C', false),
('Cold Storage', 'DEP002', 1, 'MAIN', 500.0, '2-8°C', true),
('Branch Storage', 'DEP003', 2, 'SECONDARY', 300.0, '15-25°C', false);

-- Insert customer tariffs
INSERT INTO cat_tarif (description, price_ttc)
VALUES 
('Standard', 100.00),
('Premium', 150.00),
('VIP', 200.00);

-- Insert customer groups
INSERT INTO customer_groups (name, description)
VALUES 
('Regular Customers', 'Standard customer group'),
('Premium Customers', 'High-value customers'),
('VIP Customers', 'Very important customers');

-- Insert customers
INSERT INTO customers (
    ct_num, ice, description, telephone, email, address, code_postal, ville, country, 
    cate_tarif_id, is_vip, loyalty_points, total_orders, total_spent, average_order_value,
    last_order_date, credit_score, credit_limit, outstanding_balance, business_activity,
    annual_turnover, contract_start_date, contract_end_date, active, created_at, updated_at
)
VALUES 
('CUST001', 'ICE001', 'Restaurant Al Fassia', '+212522111111', 'contact@alfassia.ma', 
 '123 Avenue Hassan II', '20000', 'Casablanca', 'Morocco', 1, false,
 150, 45, 67500.00, 1500.00, CURRENT_TIMESTAMP - INTERVAL '2 days',
 750, 50000.00, 12500.00, 'Restaurant', 250000.00,
 CURRENT_TIMESTAMP - INTERVAL '1 year', CURRENT_TIMESTAMP + INTERVAL '2 years',
 true, CURRENT_TIMESTAMP - INTERVAL '1 year', CURRENT_TIMESTAMP),

('CUST002', 'ICE002', 'Hotel Royal Mansour', '+212522222222', 'contact@royalmansour.ma', 
 '45 Rue Mohammed V', '10000', 'Rabat', 'Morocco', 2, true,
 500, 120, 180000.00, 1500.00, CURRENT_TIMESTAMP - INTERVAL '1 day',
 850, 200000.00, 45000.00, 'Luxury Hotel', 800000.00,
 CURRENT_TIMESTAMP - INTERVAL '2 years', CURRENT_TIMESTAMP + INTERVAL '3 years',
 true, CURRENT_TIMESTAMP - INTERVAL '2 years', CURRENT_TIMESTAMP),

('CUST003', 'ICE003', 'Cafe Clock', '+212522333333', 'contact@cafeclock.ma', 
 '78 Rue Tariq Ibn Ziad', '40000', 'Marrakech', 'Morocco', 1, false,
 75, 30, 22500.00, 750.00, CURRENT_TIMESTAMP - INTERVAL '5 days',
 650, 25000.00, 5000.00, 'Cafe & Restaurant', 120000.00,
 CURRENT_TIMESTAMP - INTERVAL '6 months', CURRENT_TIMESTAMP + INTERVAL '1 year',
 true, CURRENT_TIMESTAMP - INTERVAL '6 months', CURRENT_TIMESTAMP),

('CUST004', 'ICE004', 'Supermarket Marjane', '+212522444444', 'contact@marjane.ma',
 '90 Boulevard Anfa', '20000', 'Casablanca', 'Morocco', 2, true,
 1000, 500, 750000.00, 1500.00, CURRENT_TIMESTAMP - INTERVAL '1 week',
 900, 1000000.00, 150000.00, 'Retail', 5000000.00,
 CURRENT_TIMESTAMP - INTERVAL '3 years', CURRENT_TIMESTAMP + INTERVAL '5 years',
 true, CURRENT_TIMESTAMP - INTERVAL '3 years', CURRENT_TIMESTAMP),

('CUST005', 'ICE005', 'Bakery La Brioche', '+212522555555', 'contact@labrioche.ma',
 '15 Rue Mohammed V', '40000', 'Marrakech', 'Morocco', 1, false,
 50, 25, 12500.00, 500.00, CURRENT_TIMESTAMP - INTERVAL '10 days',
 600, 15000.00, 2000.00, 'Bakery', 80000.00,
 CURRENT_TIMESTAMP - INTERVAL '3 months', CURRENT_TIMESTAMP + INTERVAL '9 months',
 true, CURRENT_TIMESTAMP - INTERVAL '3 months', CURRENT_TIMESTAMP);

-- Insert customer group members
INSERT INTO customer_group_members (customer_id, group_id)
VALUES 
(1, 1), -- Al Fassia in Regular Customers
(2, 3), -- Royal Mansour in VIP Customers
(3, 1); -- Cafe Clock in Regular Customers

-- Insert product families
INSERT INTO product_families (code, name, description)
VALUES 
('PF001', 'Fresh Produce', 'Fresh fruits and vegetables'),
('PF002', 'Dairy Products', 'Milk, cheese, and dairy items'),
('PF003', 'Meat Products', 'Fresh and frozen meat products');

-- Insert products
INSERT INTO products (reference, sku, title, description, barcode, family_code, category1, sale_price, unit_price, price_including_tax, deliverable)
VALUES 
('P001', 'SKU001', 'Fresh Tomatoes', 'Premium quality tomatoes', '1234567890123', 'PF001', 'Vegetables', 15.00, 12.00, 15.00, true),
('P002', 'SKU002', 'Whole Milk', 'Fresh whole milk 1L', '2345678901234', 'PF002', 'Dairy', 12.00, 10.00, 12.00, true),
('P003', 'SKU003', 'Beef Steak', 'Premium beef steak 500g', '3456789012345', 'PF003', 'Meat', 120.00, 100.00, 120.00, true);

-- Insert product-customer relations
INSERT INTO products_customer (reference_product, reference_customer, category, coef, prix_ttc)
VALUES 
(1, 1, 'Vegetables', 1.0, 15.00),
(2, 1, 'Dairy', 1.0, 12.00),
(3, 2, 'Meat', 1.2, 144.00);

-- Insert product stocks
INSERT INTO product_stocks (product_id, product_name, depot_id, depot_name, quantity, unit_cost, quality_status)
VALUES 
(1, 'Fresh Tomatoes', 1, 'Main Storage', 1000.00, 10.00, 'INSPECTED'),
(2, 'Whole Milk', 2, 'Cold Storage', 500.00, 8.00, 'INSPECTED'),
(3, 'Beef Steak', 2, 'Cold Storage', 200.00, 90.00, 'INSPECTED');

-- Insert rewards
INSERT INTO rewards (reward_type, value, target_entity_type)
VALUES 
('PERCENTAGE', 10.00, 'PRODUCT'),
('FIXED_AMOUNT', 50.00, 'CART'),
('LOYALTY_POINTS', 100.00, 'CART');

-- Insert promotions
INSERT INTO promotions (promo_code, name, description, start_date, end_date, priority, is_active, min_purchase_amount)
VALUES 
('SUMMER10', 'Summer Sale 10%', '10% off on all products', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days', 1, true, 100.00),
('WELCOME50', 'Welcome Discount', '50 MAD off on first order', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '90 days', 2, true, 200.00);

-- Insert promotion rules
INSERT INTO promotion_rules (name, condition_logic, calculation_method, breakpoint_type, promotion_id)
VALUES 
('Summer Sale Rule', 'ALL', 'BRACKET', 'AMOUNT', 1),
('Welcome Discount Rule', 'ALL', 'BRACKET', 'AMOUNT', 2);

-- Insert promotion conditions
INSERT INTO promotion_conditions (condition_type, attribute, operator, value, rule_id)
VALUES 
('CART_SUBTOTAL', 'amount', 'GREATER_THAN_OR_EQUAL', '100', 1),
('CUSTOMER_IN_GROUP', 'group_id', 'EQUAL', '1', 2);

-- Insert promotion tiers
INSERT INTO promotion_tiers (minimum_threshold, rule_id, reward_id)
VALUES 
(100.00, 1, 1),
(200.00, 2, 2);

-- Insert orders
INSERT INTO orders (customer_id, subtotal, total, total_discount, status, payment_method, payment_status)
VALUES 
(1, 150.00, 135.00, 15.00, 'COMPLETED', 'CASH', 'PAID'),
(2, 300.00, 250.00, 50.00, 'COMPLETED', 'CREDIT_CARD', 'PAID');

-- Insert order items
INSERT INTO order_items (order_id, product_id, product_name, price, quantity, sku, discount_amount)
VALUES 
(1, 1, 'Fresh Tomatoes', 15.00, 10, 'SKU001', 15.00),
(2, 3, 'Beef Steak', 120.00, 2, 'SKU003', 50.00); 