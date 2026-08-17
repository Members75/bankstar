CREATE TABLE IF NOT EXISTS dynamic_rules
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    product_name VARCHAR
(
    255
) NOT NULL,
    product_id UUID NOT NULL,
    product_text TEXT,
    rule_json JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );