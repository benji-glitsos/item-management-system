CREATE DOMAIN name AS VARCHAR(255)
CHECK (
    not_empty(VALUE)
);
