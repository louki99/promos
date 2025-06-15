-- Insert dummy data for testing and development

-- Insert sites
INSERT INTO sites (id, created_at, updated_at, is_active, site_code, description, address_line1, city, country, postal_code, contact_email, contact_phone) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'SITE001', 'Main Warehouse', '123 Warehouse Ave', 'New York', 'USA', '10001', 'warehouse1@foodplus.com', '+1234567890'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'SITE002', 'North Branch', '456 North St', 'Chicago', 'USA', '60601', 'warehouse2@foodplus.com', '+1987654321'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'SITE003', 'South Branch', '789 South Blvd', 'Los Angeles', 'USA', '90001', 'warehouse3@foodplus.com', '+1122334455');

-- Insert cat_tarif
INSERT INTO cat_tarif (id, created_at, updated_at, price_ttc, description) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.00, 'Standard Pricing'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.00, 'Premium Pricing'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.00, 'Wholesale Pricing');

-- Insert categories
INSERT INTO categories (id, created_at, updated_at, is_active, parent_id, level, code, description, name) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, null, 1, 'CAT001', 'Fresh Food', 'Fresh Food'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 1, 2, 'CAT002', 'Fruits', 'Fruits'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 1, 2, 'CAT003', 'Vegetables', 'Vegetables'),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, null, 1, 'CAT004', 'Dairy', 'Dairy Products'),
(5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, null, 1, 'CAT005', 'Beverages', 'Beverages');

-- Insert customer_groups
INSERT INTO customer_groups (id, created_at, updated_at, description, name) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Regular Customers', 'Regular'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'VIP Customers', 'VIP'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Wholesale Customers', 'Wholesale');

-- Insert customers
INSERT INTO customers (id, created_at, updated_at, is_active, category_tarif_id, address, city, country, email, company_name, telephone, postal_code, customer_type, ct_num, ice, description) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 1, '123 Main St', 'New York', 'USA', 'john.doe@email.com', 'John Doe Company', '+1234567890', '10001', 'B2B', 'CT001', 'ICE001', 'Regular B2B Customer'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 2, '456 Park Ave', 'Los Angeles', 'USA', 'jane.smith@email.com', 'Jane Smith Corp', '+1987654321', '90001', 'B2B', 'CT002', 'ICE002', 'Premium B2B Customer'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 3, '789 Market St', 'Chicago', 'USA', 'bob.wilson@email.com', 'Bob Wilson Inc', '+1122334455', '60601', 'B2B', 'CT003', 'ICE003', 'Wholesale B2B Customer');

-- Insert customer_group_members
INSERT INTO customer_group_members (customer_id, group_id)
VALUES 
(1, 1),
(2, 2),
(3, 3);

-- Insert depots
INSERT INTO depots (id, created_at, updated_at, is_active, site_id, depot_code, description, depot_type, capacity_cubic_meters, is_refrigerated, security_level) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 1, 'DEPOT001', 'Main Storage', 'MAIN_WAREHOUSE', 1000.0, false, 1),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 2, 'DEPOT002', 'North Storage', 'DISTRIBUTION_CENTER', 800.0, true, 2),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 3, 'DEPOT003', 'South Storage', 'COLD_STORAGE', 600.0, true, 3);

-- Insert product_families
INSERT INTO product_families (id, created_at, updated_at, is_active, code, description, name) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'FAM001', 'Fresh Produce', 'Fresh Produce'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'FAM002', 'Dairy Products', 'Dairy'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'FAM003', 'Beverages', 'Beverages');

-- Insert products
INSERT INTO products (id, created_at, updated_at, family_code, reference, title, description, 
    sale_price, price_including_tax, barcode, sku, deliverable, inactive, 
    visible, is_bulk_item, is_perishable, is_wholesale_only, requires_cold_storage, 
    b2c_display_in_catalog, b2c_featured, promo_sku_points, requires_approval, 
    requires_contract, unit_price, vendable, stock_tracking) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FAM001', 'PROD001', 'Fresh Apples', 'Fresh Red Delicious Apples', 
2.99, 3.59, 'BAR001', 'SKU001', true, false, true, false, true, false, true, 
true, false, 0.00, false, false, 2.99, true, 'FIFO'),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FAM001', 'PROD002', 'Fresh Bananas', 'Fresh Yellow Bananas', 
1.99, 2.39, 'BAR002', 'SKU002', true, false, true, false, true, false, true, 
true, false, 0.00, false, false, 1.99, true, 'FIFO'),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FAM002', 'PROD003', 'Whole Milk', 'Fresh Whole Milk 1L', 
3.49, 4.19, 'BAR003', 'SKU003', true, false, true, false, true, true, true, 
true, false, 0.00, false, false, 3.49, true, 'FIFO'),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FAM003', 'PROD004', 'Orange Juice', 'Fresh Orange Juice 1L', 
4.99, 5.99, 'BAR004', 'SKU004', true, false, true, false, true, true, true, 
true, false, 0.00, false, false, 4.99, true, 'FIFO');

-- Insert product_categories
INSERT INTO product_categories (category_id, product_id)
VALUES 
(1, 1),  -- Fresh Apples in Fresh Produce category
(1, 2),  -- Fresh Bananas in Fresh Produce category
(2, 3),  -- Whole Milk in Dairy Products category
(3, 4);  -- Orange Juice in Beverages category

-- Insert product_stocks
INSERT INTO product_stocks (id, created_at, updated_at, depot_id, product_id, quantity, reserved_quantity,
    unit_cost, quality_status, product_name, depot_name, location_code, batch_number,
    storage_conditions, notes, quality_notes, min_stock_level, max_stock_level,
    reorder_point, reorder_quantity, days_of_stock, shelf_life_days) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 100.00, 0.00, 2.50, 'INSPECTED', 'Fresh Apples', 'Main Storage', 'LOC001', 'BATCH001', 'Room Temperature', 'Regular stock', 'Good quality', 20.00, 200.00, 50.00, 100.00, 30, 14),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 2, 150.00, 0.00, 1.50, 'INSPECTED', 'Fresh Bananas', 'Main Storage', 'LOC002', 'BATCH002', 'Room Temperature', 'Regular stock', 'Good quality', 30.00, 300.00, 75.00, 150.00, 30, 7),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 3, 50.00, 0.00, 3.00, 'INSPECTED', 'Whole Milk', 'North Storage', 'LOC003', 'BATCH003', 'Refrigerated', 'Cold storage required', 'Fresh batch', 10.00, 100.00, 25.00, 50.00, 7, 7),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 4, 75.00, 0.00, 4.50, 'INSPECTED', 'Orange Juice', 'North Storage', 'LOC004', 'BATCH004', 'Refrigerated', 'Cold storage required', 'Fresh batch', 15.00, 150.00, 37.50, 75.00, 7, 14);

-- Insert products_customer
INSERT INTO products_customer (id, created_at, updated_at, is_active, reference_product, reference_customer, 
    category, prix_ttc, coef, qte_mont, remise, prix_ven_nouv, remise_nouv) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 1, 1, 'FRUITS', 2.99, 1.0, 1, 0.00, null, null),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 2, 1, 'FRUITS', 1.99, 1.0, 1, 0.00, null, null),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 3, 2, 'DAIRY', 3.49, 1.0, 1, 0.00, null, null),
(4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 4, 2, 'BEVERAGES', 4.99, 1.0, 1, 0.00, null, null);

-- Insert promotions
INSERT INTO promotions (id, name, promo_code, description, start_date, end_date, 
    start_time, end_time, priority, is_exclusive, is_active, 
    max_usage_count, current_usage_count, max_usage_per_customer, 
    min_purchase_amount, apply_first_matching_rule_only, customer_group,
    index_discount, is_nested_promotion, parent_promotion_id, nested_level,
    skip_to_sequence, time_restricted) OVERRIDING SYSTEM VALUE
VALUES 
(1, 'Summer Sale 2024', 'SUMMER2024', 'Summer Sale 2024', 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days', 
    '09:00', '17:00', 1, false, true, 1000, 0, 1, 50.00, false, 'REGULAR',
    1, false, null, 0, 1, true),
(2, 'Welcome Discount', 'WELCOME10', 'Welcome Discount for New Customers', 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '90 days', 
    '08:00', '20:00', 2, true, true, 500, 0, 1, 25.00, true, 'NEW',
    2, false, null, 0, 2, true),
(3, 'Bulk Purchase Discount', 'BULK20', 'Bulk Purchase Discount', 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '60 days', 
    '00:00', '23:59', 3, false, true, 200, 0, 2, 100.00, false, 'WHOLESALE',
    3, false, null, 0, 3, false);

-- Insert promotion_rules
INSERT INTO promotion_rules (id, name, condition_logic, calculation_method, breakpoint_type, 
    promotion_id, repetition) OVERRIDING SYSTEM VALUE
VALUES 
(1, 'Summer Sale Tier 1', 'ALL', 'BRACKET', 'QUANTITY', 1, 1),
(2, 'Summer Sale Tier 2', 'ALL', 'BRACKET', 'QUANTITY', 1, 1),
(3, 'Welcome Discount Rule', 'ALL', 'BRACKET', 'AMOUNT', 2, 1),
(4, 'Bulk Discount Rule', 'ALL', 'CUMULATIVE', 'QUANTITY', 3, 1);

-- Insert rewards
INSERT INTO rewards (id, reward_type, value, target_entity_id, target_entity_type, description, family_code, is_active, is_percentage) OVERRIDING SYSTEM VALUE
VALUES 
(1, 'PERCENT_DISCOUNT_ON_ITEM', 10.00, null, null, '10% discount on selected items', 'FAM001', true, true),
(2, 'FIXED_DISCOUNT_ON_CART', 5.00, null, null, 'Fixed $5 discount on cart total', 'FAM002', true, false),
(3, 'FREE_PRODUCT', 1.00, 1, 'PRODUCT', 'Get one free product with purchase', 'FAM003', true, false);

-- Insert promotion_tiers
INSERT INTO promotion_tiers (id, minimum_threshold, rule_id, reward_id) OVERRIDING SYSTEM VALUE
VALUES 
(1, 5.00, 1, 1),
(2, 10.00, 2, 2),
(3, 20.00, 3, 3);

-- Insert promotion_customer_families
INSERT INTO promotion_customer_families (id, promotion_id, customer_family_code, start_date, end_date) OVERRIDING SYSTEM VALUE
VALUES 
(1, 1, 'REGULAR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days'),
(2, 1, 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days'),
(3, 2, 'NEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '90 days'),
(4, 3, 'WHOLESALE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '60 days');

-- Insert promotion_customer_usage
INSERT INTO promotion_customer_usage (
    customer_id, promotion_id, usage_count
)
VALUES 
(1, 1, 2),
(2, 1, 1),
(1, 2, 1),
(3, 3, 3);

-- Insert promotion_lines
INSERT INTO promotion_lines (
    id, promotion_id, paid_family_code, free_family_code,
    paid_product_id, free_product_id
)
VALUES 
(1, 1, 'FAM001', 'FAM001', 1, 2),
(2, 1, 'FAM002', 'FAM002', 3, 4),
(3, 2, 'FAM002', 'FAM002', 2, 3),
(4, 3, 'FAM003', 'FAM003', 4, 1);

-- Insert promotion_excluded_categories
INSERT INTO promotion_excluded_categories (
    promotion_id, category_id
)
VALUES 
(1, 1),
(1, 2),
(2, 3),
(3, 1);

-- Insert promotion_excluded_products
INSERT INTO promotion_excluded_products (
    promotion_id, product_id
)
VALUES 
(1, 1),
(1, 2),
(2, 3),
(3, 4);

-- Insert orders
INSERT INTO orders (id, created_at, updated_at, customer_id, order_number, 
    status, payment_status, shipping_address, shipping_city, 
    shipping_country, shipping_postal_code, subtotal, total, 
    total_discount, total_tax, shipping_cost, order_type, is_wholesale) OVERRIDING SYSTEM VALUE
VALUES 
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'ORD001', 
    'PENDING', 'PENDING', '123 Main St', 'New York', 
    'USA', '10001', 150.00, 150.00, 0.00, 0.00, 0.00, 'B2B', false),
(2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 'ORD002', 
    'CONFIRMED', 'PAID', '456 Oak Ave', 'Los Angeles', 
    'USA', '90001', 75.50, 75.50, 0.00, 0.00, 0.00, 'B2B', false),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 'ORD003', 
    'DELIVERED', 'PAID', '789 Pine Rd', 'Chicago', 
    'USA', '60601', 200.00, 200.00, 0.00, 0.00, 0.00, 'B2B', true);

-- Insert order_items
INSERT INTO order_items (id, order_id, product_id, product_family_id, product_name,
    sku, unit_price, quantity, discount_amount, tax_amount,
    total_price, consumed_quantity, created_at) OVERRIDING SYSTEM VALUE
VALUES 
(1, 1, 1, 1, 'Fresh Apples',
    'SKU001', 2.99, 10, 0.00, 0.00,
    29.90, 0, CURRENT_TIMESTAMP),
(2, 1, 2, 1, 'Fresh Oranges',
    'SKU002', 3.99, 5, 0.00, 0.00,
    19.95, 0, CURRENT_TIMESTAMP),
(3, 2, 3, 2, 'Fresh Milk',
    'SKU003', 4.99, 3, 0.00, 0.00,
    14.97, 0, CURRENT_TIMESTAMP),
(4, 3, 4, 3, 'Orange Juice',
    'SKU004', 5.99, 8, 0.00, 0.00,
    47.92, 0, CURRENT_TIMESTAMP);

-- Insert order_item_applied_promotions
INSERT INTO order_item_applied_promotions (
    order_item_id, promotion_code
)
VALUES 
(1, 'SUMMER2024'),  -- Fresh Apples with Summer Sale
(2, 'SUMMER2024'),  -- Fresh Oranges with Summer Sale
(3, 'WELCOME10'),   -- Fresh Milk with Welcome Discount
(4, 'BULK20');      -- Orange Juice with Bulk Purchase Discount 