CREATE TABLE IF NOT EXISTS followings (
   follower_id INTEGER, 
   followee_id INTEGER,
   FOREIGN KEY (follower_id) REFERENCES users (id),
   FOREIGN KEY (followee_id) REFERENCES users (id)
   
)