-- Migration: Create Utility Functions
-- Description: Creates utility functions used by other migrations
-- Version: 0.1.0
-- Date: 2024-01-15

-- Function: Update Updated At Column
-- This function automatically updates the updated_at column when a row is updated
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function: Update Updated At Column (alternative name for compatibility)
CREATE OR REPLACE FUNCTION updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql; 