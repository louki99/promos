-- Create common tables
CREATE TABLE sites (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    site_code VARCHAR(255) NOT NULL UNIQUE,
    address_line1 VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    contact_phone VARCHAR(50),
    contact_email VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    capacity_sqm DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE depots (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    depot_code VARCHAR(255) NOT NULL UNIQUE,
    site_id BIGINT REFERENCES sites(id),
    depot_type depot_type,
    capacity_cubic_meters DOUBLE PRECISION,
    temperature_range VARCHAR(100),
    is_refrigerated BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    security_level INTEGER,
    access_restrictions VARCHAR(255),
    handling_equipment VARCHAR(255),
    special_requirements TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_depots_code ON depots(depot_code); 