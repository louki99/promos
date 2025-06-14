-- Insert dummy data for testing

-- Insert sites
INSERT INTO sites (description, site_code, address_line1, city, country, postal_code, contact_phone, contact_email)
VALUES 
('Main Warehouse', 'WH001', '123 Industrial Zone', 'Casablanca', 'Morocco', '20000', '+212522000001', 'warehouse@foodplus.ma'),
('Branch Office', 'BR001', '45 Business District', 'Rabat', 'Morocco', '10000', '+212537000001', 'branch@foodplus.ma')
ON CONFLICT (site_code) DO NOTHING;

-- Insert depots using subqueries to get site_id
INSERT INTO depots (description, depot_code, site_id, depot_type, capacity_cubic_meters, temperature_range, is_refrigerated)
VALUES 
('Main Storage', 'DEP001', (SELECT id FROM sites WHERE site_code = 'WH001'), 'MAIN', 1000.0, '15-25°C', false),
('Cold Storage', 'DEP002', (SELECT id FROM sites WHERE site_code = 'WH001'), 'MAIN', 500.0, '2-8°C', true),
('Branch Storage', 'DEP003', (SELECT id FROM sites WHERE site_code = 'BR001'), 'SECONDARY', 300.0, '15-25°C', false)
ON CONFLICT (depot_code) DO NOTHING;

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
    ct_num, ice, description, telephone, email, address, postal_code, city, country, 
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
INSERT INTO product_families (description, family_code, parent_id)
VALUES 
('Fresh Produce', 'FP001', NULL),
('Dairy Products', 'DP001', NULL),
('Meat & Poultry', 'MP001', NULL),
('Bakery', 'BK001', NULL),
('Frozen Foods', 'FF001', NULL),
('Beverages', 'BV001', NULL),
('Snacks & Confectionery', 'SC001', NULL),
('Canned Goods', 'CG001', NULL),
('Dry Goods', 'DG001', NULL),
('Spices & Condiments', 'SP001', NULL)
ON CONFLICT (family_code) DO NOTHING;

-- Insert categories
INSERT INTO categories (description, category_code, parent_id)
VALUES 
('Fruits', 'FR001', (SELECT id FROM product_families WHERE family_code = 'FP001')),
('Vegetables', 'VG001', (SELECT id FROM product_families WHERE family_code = 'FP001')),
('Milk', 'ML001', (SELECT id FROM product_families WHERE family_code = 'DP001')),
('Cheese', 'CH001', (SELECT id FROM product_families WHERE family_code = 'DP001')),
('Beef', 'BF001', (SELECT id FROM product_families WHERE family_code = 'MP001')),
('Chicken', 'CK001', (SELECT id FROM product_families WHERE family_code = 'MP001')),
('Bread', 'BR001', (SELECT id FROM product_families WHERE family_code = 'BK001')),
('Pastries', 'PS001', (SELECT id FROM product_families WHERE family_code = 'BK001')),
('Frozen Vegetables', 'FV001', (SELECT id FROM product_families WHERE family_code = 'FF001')),
('Frozen Meat', 'FM001', (SELECT id FROM product_families WHERE family_code = 'FF001')),
('Soft Drinks', 'SD001', (SELECT id FROM product_families WHERE family_code = 'BV001')),
('Water', 'WT001', (SELECT id FROM product_families WHERE family_code = 'BV001')),
('Chips', 'CP001', (SELECT id FROM product_families WHERE family_code = 'SC001')),
('Chocolate', 'CL001', (SELECT id FROM product_families WHERE family_code = 'SC001')),
('Canned Vegetables', 'CV001', (SELECT id FROM product_families WHERE family_code = 'CG001')),
('Canned Fruits', 'CF001', (SELECT id FROM product_families WHERE family_code = 'CG001')),
('Rice', 'RC001', (SELECT id FROM product_families WHERE family_code = 'DG001')),
('Pasta', 'PA001', (SELECT id FROM product_families WHERE family_code = 'DG001')),
('Spices', 'SP001', (SELECT id FROM product_families WHERE family_code = 'SP001')),
('Sauces', 'SC001', (SELECT id FROM product_families WHERE family_code = 'SP001'))
ON CONFLICT (category_code) DO NOTHING;

-- Insert products
INSERT INTO products (
    reference, title, description, barcode, sale_price, sale_unit, price_including_tax,
    photo, deliverable, inactive, stock_tracking, product_family_id, category_id,
    supplier_id, supplier_reference, supplier_price, supplier_currency, supplier_min_order_qty,
    supplier_lead_time_days, supplier_payment_terms, supplier_delivery_terms,
    bulk_pack_size, bulk_pack_unit, bulk_min_order_qty, bulk_price_break_1_qty,
    bulk_price_break_1_discount, bulk_price_break_2_qty, bulk_price_break_2_discount,
    bulk_price_break_3_qty, bulk_price_break_3_discount, is_bulk_only, is_wholesale_only,
    is_featured, is_seasonal, season_start_month, season_end_month, is_perishable,
    shelf_life_days, storage_conditions, requires_cold_chain, temperature_range,
    is_organic, is_gluten_free, is_halal, is_kosher, certification_ids,
    allergens, nutritional_info, ingredients, country_of_origin, brand,
    weight_kg, dimensions_cm, customs_tariff_code, customs_description,
    customs_value_usd, customs_value_eur, customs_value_mad, customs_duty_rate,
    customs_vat_rate, customs_excise_rate, customs_other_charges, customs_notes
)
VALUES 
('APP001', 'Fresh Apples', 'Premium quality fresh apples', '1234567890123', 2.50, 'KG', true,
'apple.jpg', true, false, true, 
(SELECT id FROM product_families WHERE family_code = 'FP001'),
(SELECT id FROM categories WHERE category_code = 'FR001'),
'SUP001', 'SUP-APP-001', 1.80, 'MAD', 100,
7, 'Net 30', 'FOB',
10, 'KG', 50, 100, 5, 200, 7, 500, 10,
false, false, true, true, 9, 2, true,
30, 'Cool, dry place', false, '2-8°C',
true, true, true, false, '{1,2}',
'{}', '{"calories": 52, "protein": "0.3g", "carbs": "14g", "fat": "0.2g"}',
'Apples, preservatives', 'Morocco', 'Local Farms',
0.2, '{10,10,10}', '080810', 'Fresh apples',
0.5, 0.45, 5.0, 0, 20, 0, 0, 'Standard import'
),
('MIL001', 'Fresh Milk', 'Whole milk from local farms', '2345678901234', 1.20, 'L', true,
'milk.jpg', true, false, true,
(SELECT id FROM product_families WHERE family_code = 'DP001'),
(SELECT id FROM categories WHERE category_code = 'ML001'),
'SUP002', 'SUP-MIL-001', 0.90, 'MAD', 50,
3, 'Net 15', 'FOB',
5, 'L', 20, 100, 5, 200, 7, 500, 10,
false, false, true, false, NULL, NULL, true,
7, 'Refrigerated', true, '2-6°C',
true, true, true, false, '{1,3}',
'{"milk"}', '{"calories": 66, "protein": "3.3g", "carbs": "4.8g", "fat": "3.6g"}',
'Whole milk, vitamin D', 'Morocco', 'Local Dairy',
1.0, '{10,10,20}', '040110', 'Fresh milk',
0.3, 0.27, 3.0, 0, 20, 0, 0, 'Standard import'
)
ON CONFLICT (reference) DO NOTHING;

-- Insert product stocks
INSERT INTO product_stocks (
    product_id, product_name, depot_id, depot_name, quantity, unit_cost,
    expiry_date, quality_status, min_stock_level, max_stock_level,
    reorder_point, reorder_quantity, shelf_life_days, storage_conditions,
    batch_number, location_code
)
VALUES 
(
    (SELECT id FROM products WHERE reference = 'APP001'),
    'Fresh Apples',
    (SELECT id FROM depots WHERE depot_code = 'DEP001'),
    'Main Storage',
    1000.0, 1.80,
    CURRENT_DATE + INTERVAL '30 days',
    'INSPECTED',
    100.0, 2000.0,
    200.0, 500.0,
    30, 'Cool, dry place',
    'BATCH001',
    'A-01-01'
),
(
    (SELECT id FROM products WHERE reference = 'MIL001'),
    'Fresh Milk',
    (SELECT id FROM depots WHERE depot_code = 'DEP002'),
    'Cold Storage',
    500.0, 0.90,
    CURRENT_DATE + INTERVAL '7 days',
    'INSPECTED',
    50.0, 1000.0,
    100.0, 200.0,
    7, 'Refrigerated',
    'BATCH002',
    'B-02-01'
)
ON CONFLICT (product_id, depot_id, batch_number) DO NOTHING;

-- Insert product-customer relations
INSERT INTO products_customer (reference_product, reference_customer, category, coef, prix_ttc)
VALUES 
(1, 1, 'Vegetables', 1.0, 15.00),
(2, 1, 'Dairy', 1.0, 12.00),
(3, 2, 'Meat', 1.2, 144.00);

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
(1, 1, 'Fresh Apples', 2.50, 10, 'APP001', 0.50),
(2, 2, 'Fresh Milk', 1.20, 5, 'MIL001', 0.30); 