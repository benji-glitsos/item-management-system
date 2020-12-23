CREATE TABLE meta (
    id SERIAL PRIMARY KEY
  , uuid UUID UNIQUE NOT NULL DEFAULT gen_random_uuid()
  , created_at TIMESTAMP NOT NULL DEFAULT NOW()
  , edited_at TIMESTAMP
  , deleted_at TIMESTAMP
  , restored_at TIMESTAMP
  , notes TEXT NOT NULL DEFAULT ''
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY
  , meta_id SMALLINT UNIQUE NOT NULL
  , username VARCHAR(20) UNIQUE NOT NULL
  , password VARCHAR(20) NOT NULL
  , email_address EMAIL_ADDRESS NOT NULL
);
