DROP SCHEMA IF EXISTS product CASCADE;

CREATE SCHEMA product;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE product.products
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS product.order_product_m_view;

CREATE MATERIALIZED VIEW product.order_product_m_view
TABLESPACE pg_default
AS
 SELECT id,
    username,
    first_name,
    last_name
   FROM product.products
WITH DATA;

refresh materialized VIEW product.order_product_m_view;

DROP function IF EXISTS product.refresh_order_product_m_view;

CREATE OR replace function product.refresh_order_product_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW product.order_product_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_product_m_view ON product.products;

CREATE trigger refresh_order_product_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON product.products FOR each statement
EXECUTE PROCEDURE product.refresh_order_product_m_view();