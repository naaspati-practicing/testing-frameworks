SELECT * FROM users WHERE id IN 
  (SELECT followee_id FROM followings WHERE 
    follower_id = (SELECT id FROM users WHERE username=:username)
  )