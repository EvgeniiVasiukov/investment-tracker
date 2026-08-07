ALTER TABLE positions
ADD CONSTRAINT uk_positions_user_ticker
UNIQUE (user_id, ticker);