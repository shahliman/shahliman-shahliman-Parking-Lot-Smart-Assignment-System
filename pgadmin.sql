CREATE TABLE IF NOT EXISTS parking_history (
    id SERIAL PRIMARY KEY,
    plate VARCHAR(20),
    vehicle_size VARCHAR(20),
    entry_time TIMESTAMP,
    exit_time TIMESTAMP,
    fee DOUBLE PRECISION,
    spot_id VARCHAR(10)
);

select * from parking_history


