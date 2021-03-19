CREATE DOMAIN metakey AS VARCHAR(22)
CHECK (
    VALUE ~ '^[-_a-zA-Z0-9]{1,8}~[-_a-zA-Z0-9]{10}$'
);
