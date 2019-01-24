CREATE TABLE IF NOT EXIST api_token (
  token VARCHAR(36) UNIQUE,
  user_id INTEGER REFERENCES user(id), 
)