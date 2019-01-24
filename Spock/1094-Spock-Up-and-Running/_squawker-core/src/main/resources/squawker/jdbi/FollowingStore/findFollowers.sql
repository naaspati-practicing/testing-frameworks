SELECT * FROM users WHERE id IN 
  (SELECT follower_id FROM followings WHERE 
    followee_id = (SELECT id FROM users WHERE username=:username)
  )