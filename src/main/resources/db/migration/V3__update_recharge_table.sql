CREATE TABLE IF NOT EXISTS recharge (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mobile_number VARCHAR(10) NOT NULL,
    plan_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    recharge_date DATETIME NOT NULL,
    expiry_date DATETIME NOT NULL,
    payment_mode VARCHAR(50) NOT NULL,
    FOREIGN KEY (plan_id) REFERENCES plan(id)
);