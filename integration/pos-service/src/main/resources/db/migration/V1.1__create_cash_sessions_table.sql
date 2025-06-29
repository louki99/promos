-- Create cash_sessions table
CREATE TABLE cash_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cashier_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    terminal_id BIGINT NOT NULL,
    opened_at TIMESTAMP NOT NULL,
    closed_at TIMESTAMP,
    initial_amount DECIMAL(12,2) NOT NULL CHECK (initial_amount > 0),
    total_sales DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (total_sales >= 0),
    cash_collected DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (cash_collected >= 0),
    status VARCHAR(10) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'CLOSED')),
    expected_cash DECIMAL(12,2),
    cash_difference DECIMAL(12,2),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_cash_sessions_cashier FOREIGN KEY (cashier_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_cash_sessions_store FOREIGN KEY (store_id) REFERENCES stores(id) ON DELETE CASCADE,
    CONSTRAINT fk_cash_sessions_terminal FOREIGN KEY (terminal_id) REFERENCES terminals(id) ON DELETE CASCADE
);

-- Add index for better performance
CREATE INDEX idx_cash_sessions_cashier_status ON cash_sessions(cashier_id, status);
CREATE INDEX idx_cash_sessions_store_status ON cash_sessions(store_id, status);
CREATE INDEX idx_cash_sessions_terminal_status ON cash_sessions(terminal_id, status);
CREATE INDEX idx_cash_sessions_opened_at ON cash_sessions(opened_at);
CREATE INDEX idx_cash_sessions_closed_at ON cash_sessions(closed_at);

-- Add foreign key constraint for cash_session_id in sales table (column already exists in V1.0)
ALTER TABLE sales ADD CONSTRAINT fk_sales_cash_session FOREIGN KEY (cash_session_id) REFERENCES cash_sessions(session_id) ON DELETE SET NULL;

-- Create index for sales cash session relationship
CREATE INDEX idx_sales_cash_session ON sales(cash_session_id);

-- Add comment to table
COMMENT ON TABLE cash_sessions IS 'Stores cash register sessions for tracking daily cash operations';
COMMENT ON COLUMN cash_sessions.session_id IS 'Unique identifier for the cash session';
COMMENT ON COLUMN cash_sessions.cashier_id IS 'Reference to the cashier (user) who opened the session';
COMMENT ON COLUMN cash_sessions.store_id IS 'Reference to the store where the session was opened';
COMMENT ON COLUMN cash_sessions.terminal_id IS 'Reference to the terminal/register used';
COMMENT ON COLUMN cash_sessions.opened_at IS 'Timestamp when the session was opened';
COMMENT ON COLUMN cash_sessions.closed_at IS 'Timestamp when the session was closed (nullable for open sessions)';
COMMENT ON COLUMN cash_sessions.initial_amount IS 'Initial cash amount when session was opened';
COMMENT ON COLUMN cash_sessions.total_sales IS 'Total sales amount during the session';
COMMENT ON COLUMN cash_sessions.cash_collected IS 'Actual cash collected when session was closed';
COMMENT ON COLUMN cash_sessions.status IS 'Session status: OPEN or CLOSED';
COMMENT ON COLUMN cash_sessions.expected_cash IS 'Expected cash amount (initial + sales)';
COMMENT ON COLUMN cash_sessions.cash_difference IS 'Difference between expected and actual cash collected';
COMMENT ON COLUMN cash_sessions.notes IS 'Additional notes about the session'; 