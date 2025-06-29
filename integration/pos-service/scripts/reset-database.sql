-- =====================================================
-- FoodPlus POS System - Database Reset Script
-- =====================================================
-- WARNING: This script will DROP and RECREATE the entire 'pos' schema
-- Use only in development environments!
-- =====================================================

-- Drop the entire pos schema and all its contents
DROP SCHEMA IF EXISTS pos CASCADE;

-- Create a fresh pos schema
CREATE SCHEMA pos;

-- Set the search path to the pos schema
SET search_path TO pos;

-- Log the reset
DO $$
BEGIN
    RAISE NOTICE 'Database reset completed successfully!';
    RAISE NOTICE 'Schema "pos" has been dropped and recreated.';
    RAISE NOTICE 'Ready for Flyway migrations.';
END $$; 