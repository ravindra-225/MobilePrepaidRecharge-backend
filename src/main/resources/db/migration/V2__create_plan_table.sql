CREATE TABLE IF NOT EXISTS plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price DOUBLE NOT NULL,
    validity INT NOT NULL,
    data VARCHAR(100),
    category VARCHAR(50) NOT NULL
);

-- Insert sample plans
INSERT INTO plan (name, description, price, validity, data, category) VALUES
('Popular Plan 1', 'Unlimited voice calls, 28 days', 249.99, 28, '2GB/day', 'POPULAR'),
('Popular Plan 2', 'free JioHotstar 90days,Unlimited voice calls 28days', 399.99, 28, '3GB/day', 'POPULAR'),
('Validity Plan 1', 'Unliited voice calls 56 days', 499.99, 56, '1GB/day', 'VALIDITY'),
('Validity Plan 2', 'Unlimited voice calls 84 days', 799.99, 84, '1.5GB/day', 'VALIDITY'),
('Data Plan 1', 'Data pack valid  upto 30 days', 199.99, 30, '10GB', 'DATA'),
('Data Plan 2', 'Data pack valid upto 30 days', 349.99, 30, '20GB', 'DATA'),
('Unlimited Data Plan 1', 'Unlimited Data, 28 days', 599.99, 28, 'Unlimited', 'UNLIMITED_DATA'),
('Unlimited Data Plan 2', 'Unlimited Data, 56 days', 999.99, 56, 'Unlimited', 'UNLIMITED_DATA'),
('Short Plan', '1GB/day, 2 days', 49.99, 2, '1GB/day', 'SHORT_TERM');