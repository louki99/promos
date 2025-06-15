-- Add indexes for frequently queried columns and foreign keys

-- Sites table indexes
CREATE INDEX IF NOT EXISTS idx_sites_site_code ON sites(site_code);
CREATE INDEX IF NOT EXISTS idx_sites_is_active ON sites(is_active);
CREATE INDEX IF NOT EXISTS idx_sites_city ON sites(city);
CREATE INDEX IF NOT EXISTS idx_sites_country ON sites(country);

-- Categories table indexes
CREATE INDEX IF NOT EXISTS idx_categories_parent_id ON categories(parent_id);
CREATE INDEX IF NOT EXISTS idx_categories_is_active ON categories(is_active);
CREATE INDEX IF NOT EXISTS idx_categories_name ON categories(name);
CREATE INDEX IF NOT EXISTS idx_categories_code ON categories(code);
CREATE INDEX IF NOT EXISTS idx_categories_level ON categories(level);

-- Customer groups table indexes
CREATE INDEX IF NOT EXISTS idx_customer_groups_name ON customer_groups(name);

-- Customers table indexes
CREATE INDEX IF NOT EXISTS idx_customers_category_tarif_id ON customers(category_tarif_id);
CREATE INDEX IF NOT EXISTS idx_customers_is_active ON customers(is_active);
CREATE INDEX IF NOT EXISTS idx_customers_email ON customers(email);
CREATE INDEX IF NOT EXISTS idx_customers_telephone ON customers(telephone);
CREATE INDEX IF NOT EXISTS idx_customers_company_name ON customers(company_name);
CREATE INDEX IF NOT EXISTS idx_customers_city ON customers(city);
CREATE INDEX IF NOT EXISTS idx_customers_country ON customers(country);
CREATE INDEX IF NOT EXISTS idx_customers_customer_type ON customers(customer_type);
CREATE INDEX IF NOT EXISTS idx_customers_ct_num ON customers(ct_num);
CREATE INDEX IF NOT EXISTS idx_customers_ice ON customers(ice);
CREATE INDEX IF NOT EXISTS idx_customers_is_vip ON customers(is_vip);
CREATE INDEX IF NOT EXISTS idx_customers_credit_rating ON customers(credit_rating);
CREATE INDEX IF NOT EXISTS idx_customers_contract_end_date ON customers(contract_end_date);

-- Customer group members indexes
CREATE INDEX IF NOT EXISTS idx_customer_group_members_customer_id ON customer_group_members(customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_group_members_group_id ON customer_group_members(group_id);

-- Products table indexes
CREATE INDEX IF NOT EXISTS idx_products_family_code ON products(family_code);
CREATE INDEX IF NOT EXISTS idx_products_inactive ON products(inactive);
CREATE INDEX IF NOT EXISTS idx_products_reference ON products(reference);
CREATE INDEX IF NOT EXISTS idx_products_title ON products(title);
CREATE INDEX IF NOT EXISTS idx_products_sale_price ON products(sale_price);
CREATE INDEX IF NOT EXISTS idx_products_barcode ON products(barcode);
CREATE INDEX IF NOT EXISTS idx_products_sku ON products(sku);
CREATE INDEX IF NOT EXISTS idx_products_deliverable ON products(deliverable);
CREATE INDEX IF NOT EXISTS idx_products_visible ON products(visible);
CREATE INDEX IF NOT EXISTS idx_products_is_bulk_item ON products(is_bulk_item);
CREATE INDEX IF NOT EXISTS idx_products_is_perishable ON products(is_perishable);
CREATE INDEX IF NOT EXISTS idx_products_is_wholesale_only ON products(is_wholesale_only);
CREATE INDEX IF NOT EXISTS idx_products_requires_cold_storage ON products(requires_cold_storage);
CREATE INDEX IF NOT EXISTS idx_products_b2c_display_in_catalog ON products(b2c_display_in_catalog);
CREATE INDEX IF NOT EXISTS idx_products_b2c_featured ON products(b2c_featured);

-- Product categories indexes
CREATE INDEX IF NOT EXISTS idx_product_categories_product_id ON product_categories(product_id);
CREATE INDEX IF NOT EXISTS idx_product_categories_category_id ON product_categories(category_id);

-- Product stocks indexes
CREATE INDEX IF NOT EXISTS idx_product_stocks_depot_id ON product_stocks(depot_id);
CREATE INDEX IF NOT EXISTS idx_product_stocks_product_id ON product_stocks(product_id);
CREATE INDEX IF NOT EXISTS idx_product_stocks_quantity ON product_stocks(quantity);
CREATE INDEX IF NOT EXISTS idx_product_stocks_expiry_date ON product_stocks(expiry_date);
CREATE INDEX IF NOT EXISTS idx_product_stocks_quality_status ON product_stocks(quality_status);
CREATE INDEX IF NOT EXISTS idx_product_stocks_batch_number ON product_stocks(batch_number);
CREATE INDEX IF NOT EXISTS idx_product_stocks_location_code ON product_stocks(location_code);

-- Products customer indexes
CREATE INDEX IF NOT EXISTS idx_products_customer_reference_product ON products_customer(reference_product);
CREATE INDEX IF NOT EXISTS idx_products_customer_reference_customer ON products_customer(reference_customer);
CREATE INDEX IF NOT EXISTS idx_products_customer_is_active ON products_customer(is_active);
CREATE INDEX IF NOT EXISTS idx_products_customer_category ON products_customer(category);

-- Promotions table indexes
CREATE INDEX IF NOT EXISTS idx_promotions_promo_code ON promotions(promo_code);
CREATE INDEX IF NOT EXISTS idx_promotions_is_active ON promotions(is_active);
CREATE INDEX IF NOT EXISTS idx_promotions_start_date ON promotions(start_date);
CREATE INDEX IF NOT EXISTS idx_promotions_end_date ON promotions(end_date);
CREATE INDEX IF NOT EXISTS idx_promotions_priority ON promotions(priority);
CREATE INDEX IF NOT EXISTS idx_promotions_is_exclusive ON promotions(is_exclusive);
CREATE INDEX IF NOT EXISTS idx_promotions_min_purchase ON promotions(min_purchase_amount);
CREATE INDEX IF NOT EXISTS idx_promotions_usage_limits ON promotions(max_usage_count, max_usage_per_customer);

-- Promotion rules indexes
CREATE INDEX IF NOT EXISTS idx_promotion_rules_promotion_id ON promotion_rules(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_rules_breakpoint_type ON promotion_rules(breakpoint_type);
CREATE INDEX IF NOT EXISTS idx_promotion_rules_calculation_method ON promotion_rules(calculation_method);

-- Promotion conditions indexes
CREATE INDEX IF NOT EXISTS idx_promotion_conditions_rule_id ON promotion_conditions(rule_id);
CREATE INDEX IF NOT EXISTS idx_promotion_conditions_condition_type ON promotion_conditions(condition_type);
CREATE INDEX IF NOT EXISTS idx_promotion_conditions_customer_group_id ON promotion_conditions(customer_group_id);

-- Promotion tiers indexes
CREATE INDEX IF NOT EXISTS idx_promotion_tiers_rule_id ON promotion_tiers(rule_id);
CREATE INDEX IF NOT EXISTS idx_promotion_tiers_minimum_threshold ON promotion_tiers(minimum_threshold);
CREATE INDEX IF NOT EXISTS idx_promotion_tiers_reward_id ON promotion_tiers(reward_id);

-- Rewards indexes
CREATE INDEX IF NOT EXISTS idx_rewards_reward_type ON rewards(reward_type);
CREATE INDEX IF NOT EXISTS idx_rewards_target_entity_id ON rewards(target_entity_id);

-- Promotion customer families indexes
CREATE INDEX IF NOT EXISTS idx_promotion_customer_families_promotion_id ON promotion_customer_families(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_customer_families_customer_family_code ON promotion_customer_families(customer_family_code);
CREATE INDEX IF NOT EXISTS idx_promotion_customer_families_dates ON promotion_customer_families(start_date, end_date);

-- Promotion customer usage indexes
CREATE INDEX IF NOT EXISTS idx_promotion_customer_usage_customer_id ON promotion_customer_usage(customer_id);
CREATE INDEX IF NOT EXISTS idx_promotion_customer_usage_promotion_id ON promotion_customer_usage(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_customer_usage_usage_count ON promotion_customer_usage(usage_count);

-- Promotion dynamic conditions indexes
CREATE INDEX IF NOT EXISTS idx_promotion_dynamic_conditions_promotion_id ON promotion_dynamic_conditions(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_dynamic_conditions_condition_type ON promotion_dynamic_conditions(condition_type);
CREATE INDEX IF NOT EXISTS idx_promotion_dynamic_conditions_is_active ON promotion_dynamic_conditions(is_active);

-- Promotion excluded categories indexes
CREATE INDEX IF NOT EXISTS idx_promotion_excluded_categories_promotion_id ON promotion_excluded_categories(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_excluded_categories_category_id ON promotion_excluded_categories(category_id);

-- Promotion excluded products indexes
CREATE INDEX IF NOT EXISTS idx_promotion_excluded_products_promotion_id ON promotion_excluded_products(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_excluded_products_product_id ON promotion_excluded_products(product_id);

-- Promotion lines indexes
CREATE INDEX IF NOT EXISTS idx_promotion_lines_promotion_id ON promotion_lines(promotion_id);
CREATE INDEX IF NOT EXISTS idx_promotion_lines_paid_family_code ON promotion_lines(paid_family_code);
CREATE INDEX IF NOT EXISTS idx_promotion_lines_free_family_code ON promotion_lines(free_family_code);

-- Orders table indexes
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
CREATE INDEX IF NOT EXISTS idx_orders_total ON orders(total);
CREATE INDEX IF NOT EXISTS idx_orders_order_number ON orders(order_number);
CREATE INDEX IF NOT EXISTS idx_orders_payment_status ON orders(payment_status);
CREATE INDEX IF NOT EXISTS idx_orders_order_type ON orders(order_type);
CREATE INDEX IF NOT EXISTS idx_orders_is_wholesale ON orders(is_wholesale);
CREATE INDEX IF NOT EXISTS idx_orders_payment_method ON orders(payment_method);
CREATE INDEX IF NOT EXISTS idx_orders_delivery_dates ON orders(preferred_delivery_date, delivered_at);

-- Order items indexes
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product_id ON order_items(product_id);
CREATE INDEX IF NOT EXISTS idx_order_items_quantity ON order_items(quantity);
CREATE INDEX IF NOT EXISTS idx_order_items_unit_price ON order_items(unit_price);
CREATE INDEX IF NOT EXISTS idx_order_items_special_pricing ON order_items(special_pricing);
CREATE INDEX IF NOT EXISTS idx_order_items_sku ON order_items(sku);

-- Order item applied promotions indexes
CREATE INDEX IF NOT EXISTS idx_order_item_applied_promotions_order_item_id ON order_item_applied_promotions(order_item_id);
CREATE INDEX IF NOT EXISTS idx_order_item_applied_promotions_promotion_code ON order_item_applied_promotions(promotion_code);

-- Composite indexes for common search patterns
CREATE INDEX IF NOT EXISTS idx_products_search ON products(title, reference, description);
CREATE INDEX IF NOT EXISTS idx_customers_search ON customers(company_name, email, telephone, ct_num);
CREATE INDEX IF NOT EXISTS idx_orders_customer_date ON orders(customer_id, created_at);
CREATE INDEX IF NOT EXISTS idx_product_stocks_depot_product ON product_stocks(depot_id, product_id);
CREATE INDEX IF NOT EXISTS idx_promotions_active_date ON promotions(is_active, start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_product_stocks_quality_expiry ON product_stocks(quality_status, expiry_date);
CREATE INDEX IF NOT EXISTS idx_orders_status_date ON orders(status, created_at);
CREATE INDEX IF NOT EXISTS idx_products_b2c_catalog ON products(b2c_display_in_catalog, visible, inactive);
CREATE INDEX IF NOT EXISTS idx_products_b2b_catalog ON products(is_wholesale_only, inactive); 