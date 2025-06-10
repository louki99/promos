-- Create promotion tables
CREATE TABLE rewards (
    id BIGSERIAL PRIMARY KEY,
    reward_type reward_type NOT NULL,
    value DECIMAL(24,6) NOT NULL,
    target_entity_id BIGINT,
    target_entity_type VARCHAR(255)
);

CREATE TABLE promotions (
    id BIGSERIAL PRIMARY KEY,
    promo_code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    priority INTEGER NOT NULL DEFAULT 0,
    is_exclusive BOOLEAN NOT NULL DEFAULT false,
    combinability_group VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    apply_first_matching_rule_only BOOLEAN DEFAULT false,
    max_usage_count INTEGER,
    current_usage_count INTEGER,
    max_usage_per_customer INTEGER,
    min_purchase_amount DECIMAL(24,6),
    skip_to_sequence INTEGER,
    index_discount INTEGER
);

CREATE TABLE promotion_rules (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    condition_logic condition_logic NOT NULL,
    calculation_method calculation_method NOT NULL,
    breakpoint_type breakpoint_type NOT NULL,
    promotion_id BIGINT REFERENCES promotions(id) ON DELETE CASCADE,
    repetition INTEGER
);

CREATE TABLE promotion_conditions (
    id BIGSERIAL PRIMARY KEY,
    condition_type condition_type NOT NULL,
    attribute VARCHAR(255) NOT NULL,
    operator operator NOT NULL,
    value VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    entity_id VARCHAR(255),
    customer_group_id BIGINT,
    required_loyalty_level INTEGER,
    payment_method VARCHAR(255),
    rule_id BIGINT REFERENCES promotion_rules(id) ON DELETE CASCADE
);

CREATE TABLE promotion_tiers (
    id BIGSERIAL PRIMARY KEY,
    minimum_threshold DECIMAL(24,6) NOT NULL,
    rule_id BIGINT REFERENCES promotion_rules(id) ON DELETE CASCADE,
    reward_id BIGINT UNIQUE REFERENCES rewards(id) ON DELETE CASCADE
);

CREATE TABLE promotion_excluded_products (
    promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL
);

CREATE TABLE promotion_excluded_categories (
    promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
    category VARCHAR(255) NOT NULL
);

CREATE TABLE promotion_customer_usage (
    promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
    customer_id BIGINT NOT NULL,
    usage_count INTEGER NOT NULL DEFAULT 0,
    last_used_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (promotion_id, customer_id)
);

CREATE TABLE promotion_customer_families (
    promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
    customer_family_id BIGINT NOT NULL,
    PRIMARY KEY (promotion_id, customer_family_id)
);

CREATE TABLE promotion_lines (
    promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
    line_number INTEGER NOT NULL,
    condition_type VARCHAR(255) NOT NULL,
    condition_value TEXT NOT NULL,
    PRIMARY KEY (promotion_id, line_number)
);

CREATE TABLE promotion_dynamic_conditions (
    promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
    condition_type VARCHAR(255) NOT NULL,
    condition_value TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_promotions_code ON promotions(promo_code);
CREATE INDEX idx_promotions_dates ON promotions(start_date, end_date);
CREATE INDEX idx_promotion_rules_promotion ON promotion_rules(promotion_id);
CREATE INDEX idx_promotion_conditions_rule ON promotion_conditions(rule_id);
CREATE INDEX idx_promotion_tiers_rule ON promotion_tiers(rule_id);
CREATE INDEX idx_rewards_type ON rewards(reward_type);
CREATE INDEX idx_promotion_customer_families_promotion ON promotion_customer_families(promotion_id);
CREATE INDEX idx_promotion_lines_promotion ON promotion_lines(promotion_id);
CREATE INDEX idx_promotion_dynamic_conditions_promotion ON promotion_dynamic_conditions(promotion_id); 