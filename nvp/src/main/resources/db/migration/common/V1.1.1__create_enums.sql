-- Create enum types
DO $$ BEGIN
    CREATE TYPE suivi_stock AS ENUM ('Aucun', 'Simple', 'Avance');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE quality_status AS ENUM ('INSPECTED', 'PENDING', 'REJECTED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE depot_type AS ENUM ('MAIN', 'SECONDARY', 'TEMPORARY');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE order_status AS ENUM ('DRAFT', 'PENDING', 'CONFIRMED', 'PROCESSING', 'COMPLETED', 'CANCELLED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE payment_method AS ENUM ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'MOBILE_PAYMENT');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE payment_status AS ENUM ('PENDING', 'PAID', 'FAILED', 'REFUNDED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE reward_type AS ENUM ('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_PRODUCT', 'LOYALTY_POINTS');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE condition_logic AS ENUM ('ALL', 'ANY');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE calculation_method AS ENUM ('BRACKET', 'CUMULATIVE');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE breakpoint_type AS ENUM ('AMOUNT', 'QUANTITY', 'SKU_POINTS');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE condition_type AS ENUM ('CART_SUBTOTAL', 'PRODUCT_IN_CART', 'CUSTOMER_IN_GROUP', 'TIME_OF_DAY', 'DAY_OF_WEEK', 'CUSTOMER_LOYALTY_LEVEL', 'PAYMENT_METHOD');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE operator AS ENUM ('EQUAL', 'NOT_EQUAL', 'GREATER_THAN', 'GREATER_THAN_OR_EQUAL', 'LESS_THAN', 'LESS_THAN_OR_EQUAL', 'CONTAINS', 'NOT_CONTAINS', 'IN', 'NOT_IN');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE order_type AS ENUM ('DINE_IN', 'TAKEAWAY', 'DELIVERY');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$; 