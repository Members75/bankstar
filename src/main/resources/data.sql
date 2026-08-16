CREATE TABLE IF NOT EXISTS products (
                                        id UUID PRIMARY KEY,
                                        name VARCHAR(255),
    type VARCHAR(50)
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            id UUID PRIMARY KEY,
                                            user_id UUID,
                                            product_id UUID,
                                            type VARCHAR(50),
    amount DECIMAL(10, 2)
    );

INSERT INTO products (id, name, type) VALUES
                                          ('22222222-2222-2222-2222-222222222222', 'Debit Card', 'DEBIT_CARD'),
                                          ('33333333-3333-3333-3333-333333333333', 'Investment Account', 'INVESTMENT'),
                                          ('44444444-4444-4444-4444-444444444444', 'Savings Account', 'SAVING');

INSERT INTO transactions (id, user_id, product_id, type, amount) VALUES
                                                                     ('11111111-1111-1111-1111-111111111111', 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '44444444-4444-4444-4444-444444444444', 'DEPOSIT', 6000.00),
                                                                     ('88888888-8888-8888-8888-888888888888', 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '22222222-2222-2222-2222-222222222222', 'PURCHASE', 100.00);