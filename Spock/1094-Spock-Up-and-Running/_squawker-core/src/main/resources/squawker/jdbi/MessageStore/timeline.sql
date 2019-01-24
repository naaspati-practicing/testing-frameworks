SELECT m.*, u.*, m.id as mid, u.id as uid FROM Messages m LEFT JOIN users u ON m.posted_by = u.id 
  WHERE u.username = :u.username OR u.id IN
    (SELECT followee_id FROM followings WHERE follower_id = 
      (SELECT id FROM users WHERE username=:u.username)
    )
