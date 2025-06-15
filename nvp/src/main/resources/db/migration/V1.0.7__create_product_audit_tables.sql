-- Create the main product audit table
CREATE TABLE product_audits (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    action VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_audits_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Create the audit changes table for storing field changes
CREATE TABLE product_audit_changes (
    audit_id BIGINT NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    change_value TEXT,
    CONSTRAINT fk_audit_changes_audit FOREIGN KEY (audit_id) REFERENCES product_audits(id) ON DELETE CASCADE
);

-- Create the audit details table for storing additional details
CREATE TABLE product_audit_details (
    audit_id BIGINT NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    field_value TEXT,
    CONSTRAINT fk_audit_details_audit FOREIGN KEY (audit_id) REFERENCES product_audits(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_product_audits_product_id ON product_audits(product_id);
CREATE INDEX idx_product_audits_user_id ON product_audits(user_id);
CREATE INDEX idx_product_audits_timestamp ON product_audits(timestamp);
CREATE INDEX idx_product_audits_action ON product_audits(action);

-- Create indexes for the changes and details tables
CREATE INDEX idx_product_audit_changes_field_name ON product_audit_changes(field_name);
CREATE INDEX idx_product_audit_details_field_name ON product_audit_details(field_name); 