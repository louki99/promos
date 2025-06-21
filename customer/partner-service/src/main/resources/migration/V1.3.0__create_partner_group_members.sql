-- Migration: Create Partner Group Members Table
-- Description: Creates the partner_group_members table for many-to-many relationship between partners and groups
-- Version: 1.3.0
-- Date: 2024-01-15

CREATE TABLE partner_group_members (
    partner_id BIGINT NOT NULL REFERENCES partners(id) ON DELETE CASCADE,
    group_id BIGINT NOT NULL REFERENCES customer_groups(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (partner_id, group_id)
);

-- Create indexes for better performance
CREATE INDEX idx_partner_group_members_partner_id ON partner_group_members(partner_id);
CREATE INDEX idx_partner_group_members_group_id ON partner_group_members(group_id);
CREATE INDEX idx_partner_group_members_created_at ON partner_group_members(created_at);

-- Create composite index for common queries
CREATE INDEX idx_partner_group_members_group_partner ON partner_group_members(group_id, partner_id); 