INSERT INTO followings (follower_id, followee_id) 
  SELECT u.id,v.id  FROM users u, users v 
    where u.username = :u.username AND v.username=:v.username 
